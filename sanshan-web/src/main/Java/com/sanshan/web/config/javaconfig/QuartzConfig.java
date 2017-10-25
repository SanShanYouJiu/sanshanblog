package com.sanshan.web.config.javaconfig;

import com.sanshan.util.BlogIdGenerate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * 定时任务配置
 */
@Configuration
@ImportResource(locations = "classpath:QuartzConfig.xml")
public class QuartzConfig {

    //@Value("${blogIdGenerate.quartz.expression}")
    //private String blogIdGenerateQuartzExpression;
    //
    //@Value("${consumer.quartz.expression}")
    //private String  consumerQuartzExpression;
    //
    @Value("${blogIdGenerate.filename}")
    private String blogIdGenerateFilename;


    @Bean(initMethod = "init")
    public BlogIdGenerate blogIdGenerate(){
        BlogIdGenerate blogIdGenerate = new BlogIdGenerate(blogIdGenerateFilename);
        return  blogIdGenerate;
    }
    //
    //@Bean(name = "jobDetail1")
    //public MethodInvokingJobDetailFactoryBean jobDetail1(BlogIdGenerate blogIdGenerate) {
    //    MethodInvokingJobDetailFactoryBean m = new MethodInvokingJobDetailFactoryBean();
    //    m.setName("blogIdGenerate-job");
    //    m.setConcurrent(false);
    //    m.setTargetObject(blogIdGenerate);
    //    m.setTargetMethod("saveMap");
    //    return m;
    //}
    //
    // @Bean(name = "jobDetail2")
    //public MethodInvokingJobDetailFactoryBean jobDetail2(ConsumerHandler consumerHandler) {
    //    MethodInvokingJobDetailFactoryBean m = new MethodInvokingJobDetailFactoryBean();
    //    m.setName("consumer-job");
    //    m.setConcurrent(false);
    //    m.setTargetObject(consumerHandler);
    //    m.setTargetMethod("process");
    //    return m;
    //}
    //
    //@Bean(name = "trigger1")
    //public CronTriggerFactoryBean trigger1(MethodInvokingJobDetailFactoryBean jobDetail1) {
    //    CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();
    //    factoryBean.setName("blogIdGenerate-work");
    //    factoryBean.setGroup("work_group");
    //    factoryBean.setJobDetail(jobDetail1.getObject());
    //    factoryBean.setCronExpression(blogIdGenerateQuartzExpression);
    //    return factoryBean;
    //}
    ////
    ////
    //@Bean(name ="trigger2" )
    //public CronTriggerFactoryBean trigger2(MethodInvokingJobDetailFactoryBean jobDetail2) {
    //    CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();
    //    factoryBean.setName("consumer-work");
    //    factoryBean.setGroup("work_group");
    //    factoryBean.setJobDetail(jobDetail2.getObject());
    //    factoryBean.setCronExpression(consumerQuartzExpression);
    //    return factoryBean;
    //}
    //
    //@Bean(name = "scheduler")
    //public SchedulerFactoryBean scheduler(CronTriggerFactoryBean trigger1, CronTriggerFactoryBean trigger2){
    //    SchedulerFactoryBean sched=new SchedulerFactoryBean();
    //    sched.setTriggers(trigger1.getObject(),trigger2.getObject());
    //    return sched;
    //}

}
