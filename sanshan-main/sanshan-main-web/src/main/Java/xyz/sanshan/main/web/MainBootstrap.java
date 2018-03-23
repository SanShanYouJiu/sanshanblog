package xyz.sanshan.main.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import xyz.sanshan.main.web.config.webaseconfig.SpringSecuityFilter;
import xyz.sanshan.main.web.config.webaseconfig.WebAppInitializer;

/**
 * 在主程序直接运行需要Servlet容器依赖
 *
 * 如果需要调整为传统Spring FrameWork方式工作 只需要将这里的继承类{@link SpringBootServletInitializer}  注释 将在{@link SpringSecuityFilter}与{@link WebAppInitializer}中的注释打开即可
 * 注意：改为微服务之后  使用原始方式会丧失掉微服务附加的功能 比如actuator的监控
 *
 *
 */
@SpringBootApplication(exclude = {RedisAutoConfiguration.class, MongoAutoConfiguration.class,ThymeleafAutoConfiguration.class, TransactionAutoConfiguration.class})
@EnableDiscoveryClient
@EnableFeignClients({"xyz.sanshan.main.service.feign"})
@EnableCircuitBreaker
public class MainBootstrap extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(MainBootstrap.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(MainBootstrap.class, args);
    }
}
