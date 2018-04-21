package xyz.sanshan.gate.server.config;

import org.springframework.context.annotation.Bean;
import xyz.sanshan.auth.security.client.interceptor.ServiceFeignInterceptor;

/**
 * Created by ace on 2017/9/12.
 */
public class FeignConfiguration {
    @Bean
    ServiceFeignInterceptor getClientTokenInterceptor(){
        return new ServiceFeignInterceptor();
    }
}
