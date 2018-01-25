package com.sanshan.web.config.webaseconfig;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;

@Configuration
@ComponentScan(basePackages ={"com.sanshan.web.config.javaconfig"},
        excludeFilters = {@ComponentScan.Filter(
                type= FilterType.ANNOTATION,
                value = {Controller.class})})
@PropertySource("file:D:/SanShanBlog.properties")
//TODO 换成Spring Boot
public class RootConfig {



}