package com.sanshan.web.config.javaconfig;

import com.sanshan.util.BlogIdGenerate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * 定时任务配置
 */
@Configuration
public class QuartzConfig {

    @Value("${storage.location}")
    private String storagelocation;

    @Value("${quartz.expression}")
    private String quartzExpression;

    @Bean(initMethod = "init")
    public  BlogIdGenerate blogIdGenerate(){
        BlogIdGenerate blogIdGenerate = new BlogIdGenerate();
        //blogIdGenerate.setFilename(storagelocation);
        return  blogIdGenerate;
    }

    @Bean
    public MethodInvokingJobDetailFactoryBean jobDetail(BlogIdGenerate job) {
        MethodInvokingJobDetailFactoryBean m = new MethodInvokingJobDetailFactoryBean();
        m.setGroup("joy_work");
        m.setName("job_work_name");
        m.setConcurrent(false);
        m.setTargetObject(job);
        m.setTargetMethod("saveMap");
        return m;
    }

    @Bean
    public CronTriggerFactoryBean myTrigger(MethodInvokingJobDetailFactoryBean jobDetail) {
        CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();
        factoryBean.setName("work_default");
        factoryBean.setGroup("work_group");
        factoryBean.setJobDetail(jobDetail.getObject());
        factoryBean.setCronExpression(quartzExpression);
        return factoryBean;
    }



    @Bean
    public SchedulerFactoryBean scheduler(CronTriggerFactoryBean myTrigger){
        SchedulerFactoryBean sched=new SchedulerFactoryBean();
        sched.setTriggers(myTrigger.getObject());
        return sched;
    }


}
