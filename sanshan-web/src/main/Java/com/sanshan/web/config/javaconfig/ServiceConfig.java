package com.sanshan.web.config.javaconfig;

import com.baidu.ueditor.upload.StorageManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@ComponentScan(value = {"com.sanshan.service"})
@PropertySource("file:D:/SanShanBlog.properties")
public class ServiceConfig {


    @Bean
    public StorageManager storageManager(){
     return new StorageManager();
 }


    @Value("${mail.host}")
    private String mailhost;
    @Value("${mail.username}")
    private String mailusername;
    @Value("${mail.password}")
    private String mailpassword;
    @Value("${mail.port}")
    private Integer port;

     @Bean
    public JavaMailSenderImpl javaMailSender(){
         JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
         mailSender.setHost(mailhost);
         mailSender.setPort(port);
         mailSender.setUsername(mailusername);
         mailSender.setPassword(mailpassword);
         mailSender.setDefaultEncoding("UTF-8");
         return mailSender;
     }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
