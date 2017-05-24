package com.sanshan.web.config.javaconfig.auxiliary;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Blog变动监听
 */
@Slf4j
@Component
public class BlogOperationListener {

    @Resource
    private RedisTemplate redisTemplate;

    public void handle(String  mesType){
       log.info("receive mesType is {}",mesType);
       log.info("暂时未做处理 {}"+mesType);
    }



}
