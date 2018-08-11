package xyz.sanshan.main.service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import xyz.sanshan.main.service.editor.BlogIdGenerate;

/**
 * @author sanshan <sanshan@maihaoche.com>
 * @date 2018-07-08
 */
@Configuration
@EnableScheduling
public class SchedulerConfig {

    @Value("${blogIdGenerate.filename}")
    private String blogIdGenerateFilename;

    @Bean(initMethod = "init")
    public BlogIdGenerate blogIdGenerate(){
        BlogIdGenerate blogIdGenerate = new BlogIdGenerate(blogIdGenerateFilename);
        return  blogIdGenerate;
    }

}
