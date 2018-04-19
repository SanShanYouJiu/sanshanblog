package xyz.sanshan.auth.security.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.sanshan.auth.security.server.util.ControllerAop;

@Configuration
public class AopConfiguration {

    @Bean
    public ControllerAop controllerAop() {

        return new ControllerAop();
    }
}
