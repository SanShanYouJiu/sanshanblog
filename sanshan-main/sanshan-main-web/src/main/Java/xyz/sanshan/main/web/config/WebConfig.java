package xyz.sanshan.main.web.config;

import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.util.IntrospectorCleanupListener;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;
import xyz.sanshan.main.web.api.ueditor.upload.StorageManager;
import xyz.sanshan.main.web.config.aop.ControllerAop;

import java.util.List;

@Configuration
@EnableWebMvc
@ComponentScan(value = "xyz.sanshan.main.web.controller",excludeFilters = {
        @ComponentScan.Filter(
                type= FilterType.ANNOTATION,
                value = {Configuration.class,Service.class, Repository.class})
})
@EnableAspectJAutoProxy(proxyTargetClass = true)//开启切面
public class WebConfig extends WebMvcConfigurerAdapter {

    @Bean
    public ViewResolver viewResolver(SpringTemplateEngine springTemplateEngine) {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setCharacterEncoding("utf-8");
        viewResolver.setTemplateEngine(springTemplateEngine);
        return viewResolver;
    }

    @Bean
    public StorageManager storageManager(){
        return new StorageManager();
    }

    @Bean
    public SpringTemplateEngine templateEngine(TemplateResolver templateResolver) {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.addDialect(new SpringSecurityDialect());//注册Spring Security方言
        templateEngine.setTemplateResolver(templateResolver);
        return templateEngine;
    }


    @Bean
    public TemplateResolver templateResolver() {
        TemplateResolver templateResolver = new ServletContextTemplateResolver();
        templateResolver.setCharacterEncoding("utf-8");
        templateResolver.setPrefix("/WEB-INF/views/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML5");
        return templateResolver;
    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(new String[]{"/assets"}).addResourceLocations("/assets");
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
//        super.configureDefaultServletHandling(configurer);
        configurer.enable();
    }

    /*multipart CommonsMultipartResolver解析器*/
    @Bean
    public  MultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(2048000);
        return multipartResolver;
    }



    @Bean
    public ControllerAop controllerAop(){
        return new ControllerAop();
    }

    /**
     * 允许请求到外部的Listener
     * @return
     */
   @Bean
   public ServletListenerRegistrationBean<RequestContextListener> requestContextListener(){
       return new ServletListenerRegistrationBean<RequestContextListener>(new RequestContextListener());
   }

    /**
     * 主要负责处理由　JavaBeans Introspector的使用而引起的缓冲泄露
     * @return
     */
   @Bean
   public ServletListenerRegistrationBean<IntrospectorCleanupListener> introspectorCleanupListener() {
        return  new ServletListenerRegistrationBean<>(new IntrospectorCleanupListener());
   }


   //@Bean
   //public FilterRegistrationBean  filterRegistrationBean(){
   //    FilterRegistrationBean charactEncodingFilter = new FilterRegistrationBean();
   //    charactEncodingFilter.addUrlPatterns("/main/**");
   //    charactEncodingFilter.setFilter(new CharacterEncodingFilter());
   //    Map<String, String> initMap = new HashMap<>(10);
   //    initMap.put("encoding", "utf-8");
   //    charactEncodingFilter.setInitParameters(initMap);
   //    return  charactEncodingFilter;
   //}

    @Autowired
    private ResourceHttpMessageConverter resourceHttpMessageConverter;

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig config = new FastJsonConfig();
        config.setDateFormat("yyyy-MM-dd HH:mm:ss");
        fastJsonHttpMessageConverter.setFastJsonConfig(config);
        converters.add(fastJsonHttpMessageConverter);
        converters.add(resourceHttpMessageConverter);
        super.configureMessageConverters(converters);
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

}

