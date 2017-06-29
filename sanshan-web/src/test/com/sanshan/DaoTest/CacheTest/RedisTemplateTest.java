package com.sanshan.DaoTest.CacheTest;

import com.sanshan.web.config.javaconfig.RedisCacheConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { RedisCacheConfig.class})
public class RedisTemplateTest {

    @Autowired
    RedisTemplate<String,String> template;

    @Test
    public void  cachetTest(){

    }

    @Test
    public void templateTest() {
        template.opsForHash();
    }
}
