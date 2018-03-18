package xyz.sanshan.main.web.config.webaseconfig;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;

@Configuration
@ComponentScan(basePackages ={"xyz.sanshan.main.web.config.javaconfig"},
        excludeFilters = {@ComponentScan.Filter(
                type= FilterType.ANNOTATION,
                value = {Controller.class})})
//@PropertySource("file:/etc/sanshanblog/SanShan-main.properties")
@PropertySource("file:D:/SanShan-main.properties")
public class RootConfig {



}