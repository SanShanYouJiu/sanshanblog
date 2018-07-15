package xyz.sanshan.main.service.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import xyz.sanshan.main.service.consumer.accept.VoteConsumer;

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


    @Scheduled(cron = "${consumer.quartz.expression:*/5 * * * * ?}")
    public void accept() {
        if (log.isDebugEnabled()) {
            log.debug("处理consumer中的数据");
        }
        voteConsumer.voteConsumerProcess();
        UEditorFileConsumer.ueditorConsumer();
    }




}
