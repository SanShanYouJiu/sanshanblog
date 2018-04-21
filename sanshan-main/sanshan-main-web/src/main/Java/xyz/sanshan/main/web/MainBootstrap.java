package xyz.sanshan.main.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import xyz.sanshan.auth.security.client.EnableSanShanAuthClient;

/**
 * 在主程序直接运行需要Servlet容器依赖
 *
 */
@SpringBootApplication(exclude = {RedisAutoConfiguration.class, MongoAutoConfiguration.class,ThymeleafAutoConfiguration.class, TransactionAutoConfiguration.class})
@EnableDiscoveryClient
@EnableFeignClients({"xyz.sanshan.main.service.feign","xyz.sanshan.auth.security.client.feign"})
@EnableCircuitBreaker
@EnableSanShanAuthClient
@ServletComponentScan("xyz.sanshan.main.web.config.javaconfig.druid")
public class MainBootstrap extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(MainBootstrap.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(MainBootstrap.class, args);
    }
}
