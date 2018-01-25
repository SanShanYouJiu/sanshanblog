package com.sanshan.web.config.webaseconfig;

import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import com.sanshan.web.config.javaconfig.auxiliary.ControllerAop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

import java.util.List;
import java.util.Properties;

@Configuration
@EnableWebMvc
@ComponentScan(value = "com.sanshan.web.controller",excludeFilters = {
        @ComponentScan.Filter(
                type= FilterType.ANNOTATION,
                value = {Configuration.class})
})
@PropertySource("file:D:/SanShanBlog.properties")
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
    public SpringTemplateEngine templateEngine(TemplateResolver templateResolver) {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
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

    @Bean
    public ControllerAop controllerAop(){
        return new ControllerAop();
    }
}

