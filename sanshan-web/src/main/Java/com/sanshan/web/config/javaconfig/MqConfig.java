package com.sanshan.web.config.javaconfig;


import com.sanshan.web.config.javaconfig.auxiliary.BlogOperationListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import java.util.*;

/**
 用的是Redis自带的 消息发布订阅配置 这个Redis自带的MQ性能不是很好 暂时在这里吧
 */
@Configuration
public class MqConfig {
    @Autowired
    JedisConnectionFactory connectionFactory;

    @Autowired
    BlogOperationListener listener;

    @Bean
    public PatternTopic cacheTopic(){
        PatternTopic patternTopic = new PatternTopic("blog");
        return patternTopic;
    }

    @Bean
     public RedisMessageListenerContainer redisMessageListenerContainer(MessageListenerAdapter listenerAdapter,
                                                                        PatternTopic patternTopic){
         RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
         redisMessageListenerContainer.setConnectionFactory(connectionFactory);
         Map<MessageListenerAdapter, Collection<? extends Topic>>map = new HashMap<>(5);
         List<Topic> list = new ArrayList<Topic>();
         list.add(patternTopic);
         map.put(listenerAdapter, list);
         redisMessageListenerContainer.setMessageListeners(map);
         return redisMessageListenerContainer;
     }


     @Bean
     public MessageListenerAdapter listenerAdapter(){
         MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(listener,
                 "handle");
        return messageListenerAdapter;
     }


}
