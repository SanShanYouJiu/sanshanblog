package xyz.sanshan.auth.security.client.configuration;

import org.springframework.cloud.bus.jackson.RemoteApplicationEventScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import xyz.sanshan.auth.security.client.config.ServiceAuthConfig;
import xyz.sanshan.auth.security.client.config.UserAuthConfig;

/**
 */
@Configuration
@ComponentScan({"xyz.sanshan.auth.security.client","xyz.sanshan.auth.security.common.event"})
@RemoteApplicationEventScan(basePackages = "xyz.sanshan.auth.security.common.event")
public class AutoConfiguration {
    @Bean
   public ServiceAuthConfig getServiceAuthConfig(){
        return new ServiceAuthConfig();
    }

    @Bean
    public  UserAuthConfig getUserAuthConfig(){
        return new UserAuthConfig();
    }

}
