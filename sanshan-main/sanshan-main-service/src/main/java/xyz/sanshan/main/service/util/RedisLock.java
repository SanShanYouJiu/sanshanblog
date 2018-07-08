package xyz.sanshan.main.service.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * redis分布式锁 实现类
 */
@Slf4j
public class RedisLock {

    private RedisTemplate<String,String> redisTemplate;

    /**
     * 锁的名字
     */
    private String lockKey;
    /**
     * 锁持有超时
     * 防止线程在入锁以后，无限的执行下去，让锁无法释放
     */

    private long expireMsecs = 60 * 1000;

    /**
     * 锁等待超时
     * 防止线程饥饿，永远没有入锁执行代码的机会
     */
    private long timeoutMsecs = 10 * 1000;

    private static final long DEFAULT_ACQUIRY_RESOLUTION_MILLIS = 100;

    /**
     * 过期时间
     * 获得锁的线程会把锁的过期时间存储起来
     */
    private ThreadLocal<String> expireTimeThreadLocal = new ThreadLocal<>();

    /**
     * 锁标志
     *
     * 这里需不需要保证可见性值得讨论, 因为是分布式的锁,
     * 1.同一个jvm的多个线程使用不同的锁对象其实也是可以的, 这种情况下不需要保证可见性
     * 2.同一个jvm的多个线程使用同一个锁对象, 那可见性就必须要保证了.
     */
    private volatile boolean locked;

    private RedisLock(RedisTemplate redisTemplate, String lockKey, Long timeoutMsecs, Long expireMsecs){
        this.redisTemplate = redisTemplate;
        this.lockKey = lockKey;
        if(timeoutMsecs != null){
            this.timeoutMsecs = timeoutMsecs;
        }
        if(expireMsecs != null){
            this.expireMsecs = expireMsecs;
        }
    }

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder{
        private RedisTemplate<String,String> redisTemplate;
        private String lockKey;
        private Long timeoutMsecs;
        private Long expireMsecs;

        public Builder lockKey(String lockKey){
            this.lockKey = lockKey;
            return this;
        }

        public Builder redisTemplate(RedisTemplate<String,String> redisTemplate){
            this.redisTemplate = redisTemplate;
            return this;
        }

        public Builder timeoutMsecs(Long timeoutMsecs){
            this.timeoutMsecs = timeoutMsecs;
            return this;
        }

        public Builder expireMsecs(Long expireMsecs){
            this.expireMsecs = expireMsecs;
            return this;
        }

        public RedisLock build(){
            return new RedisLock(redisTemplate,lockKey,timeoutMsecs,expireMsecs);
        }
    }


    public void execute(Runnable runnable) {
        execute(0,runnable);
    }

    public void execute(long sleepMsecs,Runnable runnable) {
        //这里可能多台服务器同时消费，所以加分布式锁，只要一台服务器消费即可
        try {
            if(this.lock()) {
                //需要加锁的代码
                runnable.run();
                /* 延时1分钟释放锁，来消除多台服务器时间不同步的带来的问题
                   若两台服务器30秒误差
                   A服务器先执行，处理完逻辑后，立即释放锁，用时5秒
                   此时B服务器还没有开始执行，25秒后，B开始执行肯定是可以获取锁，
                   导致该业务逻辑又处理了一次
                */
                if(sleepMsecs != 0){
                    Thread.sleep(sleepMsecs);
                }
            }else{
                log.info("get lock failed.");
            }
        } catch (InterruptedException e) {
            log.error("interruptedException:",e);
        }finally {
            this.unlock();
        }
    }

    /**
     * 获得 lock.
     *
     * 实现思路: 主要是使用了redis 的setnx命令,缓存了锁.
     * reids缓存的key是锁的key,所有的共享, value是锁的到期时间(注意:这里把过期时间放在value了,没有时间上设置其超时时间)
     * 执行过程:
     * 1.通过setnx尝试设置某个key的值,成功(当前没有这个锁)则返回,成功获得锁
     * 2.锁已经存在则获取锁的到期时间,和当前时间比较,超时的话,则设置新的值
     *
     * @return true if lock is acquired, false acquire timeouted
     * @throws InterruptedException in case of thread interruption
     *
     */
    public synchronized boolean lock() throws InterruptedException {
        long timeout = timeoutMsecs;
        while (timeout >= 0) {
            long expires = System.currentTimeMillis() + expireMsecs + 1;
            String currentExpiresTimeValue = String.valueOf(expires); //当前设置的锁到期时间
            if (redisTemplate.opsForValue().setIfAbsent(lockKey, currentExpiresTimeValue)) {
                // lock acquired
                locked = true;
                expireTimeThreadLocal.set(currentExpiresTimeValue);
                return true;
            }

            String lockExpireTimeValue = redisTemplate.opsForValue().get(lockKey); //redis里的时间
            //判断是否为空，不为空的情况下，如果被其他线程设置了值，则第二个条件判断是过不去的
            if (lockExpireTimeValue != null && isTimeExpired(lockExpireTimeValue)) {
                //获取上一个锁到期时间，并设置现在的锁到期时间
                String oldLockExpireTimeValue = redisTemplate.opsForValue().getAndSet(lockKey, currentExpiresTimeValue);
                //只有一个线程才能获取上一个线上的设置时间，因为jedis.getSet是同步的
                if (oldLockExpireTimeValue != null && oldLockExpireTimeValue.equals(lockExpireTimeValue)) {
                    //防止误删（覆盖，因为key是相同的）了他人的锁——这里达不到效果，这里值会被覆盖，但是因为相差了很少的时间，所以可以接受
                    //[分布式的情况下]:如过这个时候，多个线程恰好都到了这里，但是只有一个线程的设置值和当前值相同，他才有权利获取锁
                    // lock acquired
                    locked = true;
                    expireTimeThreadLocal.set(currentExpiresTimeValue);
                    return true;
                }
            }
            timeout -= DEFAULT_ACQUIRY_RESOLUTION_MILLIS;

            /*
                延迟100 毫秒,  这里使用随机时间可能会好一点,可以防止饥饿进程的出现,即,当同时到达多个进程,
                只会有一个进程获得锁,其他的都用同样的频率进行尝试,后面有来了一些进行,也以同样的频率申请锁,这将可能导致前面来的锁得不到满足.
                使用随机的等待时间可以一定程度上保证公平性
             */
            Thread.sleep(DEFAULT_ACQUIRY_RESOLUTION_MILLIS);

        }
        return false;
    }

    /**
     * 释放 lock.
     */
    public synchronized void unlock() {
        String currentValueStr = redisTemplate.opsForValue().get(lockKey);
        //锁对应的时间未过期，且被锁，但不一定是该线程锁住的，所以不能轻易释放该锁
        if (currentValueStr != null && !isTimeExpired(currentValueStr)) {
            //如果当前锁中的过期时间与该线程的threadLocal本地变量中的时间相同，则表示该锁正在被该线程锁住，则该线程可以释放lock。
            String expireTimeStr = expireTimeThreadLocal.get();

            if (currentValueStr.equals(expireTimeStr) && locked) {
                redisTemplate.delete(lockKey);
                locked = false;
            }
        }
    }

    /**
     * 判断输入时间相对系统当前时间是否已过期
     * @param timeValue
     * @return
     */
    private boolean isTimeExpired(String timeValue) {
        return Long.parseLong(timeValue) < System.currentTimeMillis();
    }
}
