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
    //    CacheKeyGenerator cacheKeyGenerator = new CacheKeyGenerator();
    //    MarkDownBlogCacheService cacheService = new MarkDownBlogCacheService();
    //    Class clazz=cacheService.getClass();
    //    Method m1=null;
    //    try {
    //         m1 = clazz.getDeclaredMethod("queryById", Long.class);
    //        m1.invoke(cacheService,(long)1);
    //    } catch (NoSuchMethodException e) {
    //        e.printStackTrace();
    //    } catch (IllegalAccessException e) {
    //        e.printStackTrace();
    //    } catch (InvocationTargetException e) {
    //        e.printStackTrace();
    //    }
    //    cacheKeyGenerator.generate(cacheService, m1, 1);
    }

    @Test
    public void templateTest() {
        template.opsForHash();
    }
}
