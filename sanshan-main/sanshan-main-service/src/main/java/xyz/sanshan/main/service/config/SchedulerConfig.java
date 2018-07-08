package xyz.sanshan.main.service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import xyz.sanshan.main.service.editor.BlogIdGenerate;

/**
 * TODO: 存在分布式情况下任务重复消费的问题 需要分布式的全局锁
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
