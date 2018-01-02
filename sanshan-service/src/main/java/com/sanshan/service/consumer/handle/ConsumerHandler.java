package com.sanshan.service.consumer.handle;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 消费者
 * 从生产者线程中获得消息处理
 */
@Service
@Slf4j
public class ConsumerHandler {

    @Autowired
    private VoteConsumer voteConsumer;

    @Autowired
    private UeditorFileConsumer ueditorFileConsumer;


    public void process() {
        if (log.isDebugEnabled()) {
            log.debug("处理consumer中的数据");
        }
        voteConsumer.voteConsumerProcess();
        ueditorFileConsumer.ueditorConsumer();
    }




}
