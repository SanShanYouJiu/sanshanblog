package xyz.sanshan.auth.security.server.config;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Properties;

@Configuration
public class CodeConfiguration {

    @Bean
    public DefaultKaptcha captchaProducer(){
        DefaultKaptcha kaptcha = new DefaultKaptcha();
        Properties p = new Properties();
        p.setProperty("kaptcha.image.width", "100");
        p.setProperty("kaptcha.image.height", "50");
        p.setProperty("kaptcha.noise.impl", "com.google.code.kaptcha.impl.NoNoise");
        p.setProperty("kaptcha.textproducer.char.string", "0123456789abcdefghijklmnopqrstuvwxyzyo");
        p.setProperty("kaptcha.textproducer.char.length", "4");
        Config config = new Config(p);
        kaptcha.setConfig(config);
        return kaptcha;
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
}
