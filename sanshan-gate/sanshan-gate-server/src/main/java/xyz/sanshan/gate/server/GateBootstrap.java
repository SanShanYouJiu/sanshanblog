package xyz.sanshan.gate.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import xyz.sanshan.auth.security.client.EnableSanShanAuthClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnableZuulProxy
@EnableFeignClients
@EnableSanShanAuthClient
public class GateBootstrap {


    public static void main(String[] args) {
        SpringApplication.run(GateBootstrap.class, args);
    }

}
