package xyz.sanshan.main.service.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import xyz.sanshan.main.service.consumer.accept.VoteConsumer;
import xyz.sanshan.main.service.info.LockKeyEnum;
import xyz.sanshan.main.service.util.RedisLockUtil;

/**
 * 消费者
 * 从生产者线程中获得消息处理
 */
@Service
@Slf4j
public class ConsumerAccept {

    @Autowired
    private VoteConsumer voteConsumer;

    @Autowired
    private xyz.sanshan.main.service.consumer.accept.UEditorFileConsumer UEditorFileConsumer;

    @Autowired
    private RedisTemplate redisTemplate;


    @Scheduled(cron = "${consumer.quartz.expression:*/5 * * * * ?}")
    public void accept() {
        if (log.isDebugEnabled()) {
            log.debug("处理consumer中的数据");
        }
        long EXPIRE_TIME = 5 * 1000L;
        RedisLockUtil redisLock = RedisLockUtil.builder()
                .redisTemplate(redisTemplate)
                .lockKey(LockKeyEnum.NIGHT_CHECK.getKey())
                .timeoutMsecs(0L)
                .expireMsecs(EXPIRE_TIME)
                .build();
        redisLock.execute(()-> {
                    voteConsumer.voteConsumerProcess();
                    UEditorFileConsumer.ueditorConsumer();
                }
        );
    }




}
