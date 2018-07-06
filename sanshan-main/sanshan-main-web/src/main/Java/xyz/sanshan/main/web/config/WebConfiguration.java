package xyz.sanshan.main.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import xyz.sanshan.auth.security.client.interceptor.ServiceAuthInterceptor;
import xyz.sanshan.auth.security.client.interceptor.UserAuthRestInterceptor;

import java.util.ArrayList;

/**
 * Created by ace on 2017/9/8.
 */
@Configuration("MainWebConfig")
@Primary
public class WebConfiguration extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        ArrayList<String> commonPathPatterns = new ArrayList<>();
        registry.addInterceptor(getServiceAuthRestInterceptor()).addPathPatterns("/**").excludePathPatterns(commonPathPatterns.toArray(new String[]{}));
        commonPathPatterns.add("/api/user/validate");
        registry.addInterceptor(getUserAuthRestInterceptor()).addPathPatterns("/**").excludePathPatterns(commonPathPatterns.toArray(new String[]{}));
        super.addInterceptors(registry);
    }


    @Bean
    UserAuthRestInterceptor getUserAuthRestInterceptor() {
        return new UserAuthRestInterceptor();
    }

    @Bean
    ServiceAuthInterceptor getServiceAuthRestInterceptor(){
        return new ServiceAuthInterceptor();
    }

}
