package xyz.sanshan.main.web.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Controller;

@Configuration
@ComponentScan(basePackages = {"xyz.sanshan.main.web.config"
        , "xyz.sanshan.main.service.config"
        , "xyz.sanshan.main.dao.config"

},
        excludeFilters = {@ComponentScan.Filter(
                type = FilterType.ANNOTATION,
                value = {Controller.class})})
public class RootConfig {


}