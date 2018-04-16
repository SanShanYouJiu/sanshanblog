package xyz.sanshan.auth.security.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.bus.jackson.RemoteApplicationEventScan;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@SpringCloudApplication
@EnableFeignClients
@EnableEurekaClient
@MapperScan("xyz.sanshan.auth.security.server.mapper")
@RemoteApplicationEventScan(basePackages = "xyz.sanshan.auth.security.common.event")
public class AuthBootstrap {

    public static void main(String[] args) {
        SpringApplication.run(AuthBootstrap.class, args);
    }

}
