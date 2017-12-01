package com.sanshan.web.config.javaconfig;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import com.sanshan.util.CacheKeyGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.JedisPoolConfig;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
@EnableCaching(proxyTargetClass = true)
@PropertySource("file:D:/SanShanBlog.properties")
public class RedisCacheConfig  implements CachingConfigurer  {

    @Value("${redis.maxActive}")
    private int maxactive;
    @Value("${redis.maxIdle}")
    private int maxIdle;
    @Value("${redis.minIdle}")
    private int minIdle;
    @Value("${redis.maxWaitMillis}")
    private int maxWaitMillis;

    @Value("${redis.hostName}")
    private  String hostName;


    @Bean
    public CacheManager cacheManager(RedisTemplate redisTemplate) {
        return new RedisCacheManager(redisTemplate);
    }


    @Bean
    public JedisConnectionFactory jedisConnectionFactory(JedisPoolConfig jedisPoolConfig) {
        JedisConnectionFactory jedisConnectionFactory
                = new JedisConnectionFactory();
        jedisConnectionFactory.setPoolConfig(jedisPoolConfig);
        jedisConnectionFactory.setHostName(hostName);
        jedisConnectionFactory.afterPropertiesSet();
        return jedisConnectionFactory;
    }


    @Bean
    public JedisPoolConfig jedispoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(maxactive);
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMinIdle(minIdle);
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
        return jedisPoolConfig;
    }


    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory cf) {
        RedisTemplate<String, String> redis = new RedisTemplate<String, String>();
        GenericFastJsonRedisSerializer fastJsonRedisSerializer = new GenericFastJsonRedisSerializer();
        redis.setDefaultSerializer(fastJsonRedisSerializer);
        redis.setConnectionFactory(cf);
        redis.afterPropertiesSet();
        return redis;
    }


    @Bean
    public CacheManager cacheManager(net.sf.ehcache.CacheManager cm, javax.cache.CacheManager jcm,JedisConnectionFactory jedisConnectionFactory) {
        CompositeCacheManager compositeCacheManager = new CompositeCacheManager();
        List<CacheManager> managers = new ArrayList<CacheManager>();
        managers.add(new JCacheCacheManager(jcm));
        managers.add(new EhCacheCacheManager(cm));
        managers.add(new RedisCacheManager(redisTemplate(jedisConnectionFactory)));
        compositeCacheManager.setCacheManagers(managers);
        //在找不到 accountCache，且没有将 fallbackToNoOpCache 设置为 true 的情况下，系统会抛出异常
        compositeCacheManager.setFallbackToNoOpCache(true);
        return compositeCacheManager;
    }

    /**
     * @return 自定义策略生成的key
     * @description 自定义的缓存key的生成策略
     */
    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        CacheKeyGenerator keyGenerator = new CacheKeyGenerator();
        return keyGenerator;
    }


    /*只是为了设置keyGenerator而实现该接口
    * 下列方法可以不实现 (在不加@Bean的注解的情况下)
    * */
    @Override
    public CacheManager cacheManager() {
        return null;
    }
    @Override
    public CacheResolver cacheResolver() {
        return null;
    }
    @Override
    public CacheErrorHandler errorHandler() {
        return null;
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}

