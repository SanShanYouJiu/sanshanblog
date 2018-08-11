package xyz.sanshan.main.service.task.night;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import xyz.sanshan.main.service.info.LockKeyEnum;
import xyz.sanshan.main.service.util.RedisLockUtil;

/**
 * 午夜检查相关信息是否成功核对
 * @author sanshan
 */
@Service
@Slf4j
public class NightCheckHandler {

    @Autowired
    private UEditorFileUploadCheck UEditorFileUploadCheck;

    @Autowired
    private RedisTemplate redisTemplate;

   @Scheduled(cron = "${nightCheck.quartz.expression:0 0 3 * * ?}")
    public void nightCheck() {
       long EXPIRE_TIME = 5 * 1000L;
       RedisLockUtil redisLock = RedisLockUtil.builder()
               .redisTemplate(redisTemplate)
               .lockKey(LockKeyEnum.NIGHT_CHECK.getKey())
               .timeoutMsecs(0L)
               .expireMsecs(EXPIRE_TIME)
               .build();
       redisLock.execute(()->{
           log.info("午夜检测开始");
           UEditorFileUploadCheck.check();
           log.info("核对完毕");
       });
    }
}
