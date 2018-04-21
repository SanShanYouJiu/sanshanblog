package xyz.sanshan.main.web.config.webaseconfig;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Controller;

@Configuration
@ComponentScan(basePackages ={"xyz.sanshan.main.web.config.javaconfig"},
        excludeFilters = {@ComponentScan.Filter(
                type= FilterType.ANNOTATION,
                value = {Controller.class})})
public class RootConfig {



}