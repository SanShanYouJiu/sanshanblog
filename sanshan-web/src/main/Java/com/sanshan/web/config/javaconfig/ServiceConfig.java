package com.sanshan.web.config.javaconfig;

import com.baidu.ueditor.upload.StorageManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(value = {"com.sanshan.service"})
@PropertySource("file:D:/SanShanBlog.properties")
public class ServiceConfig {


    @Bean
    public StorageManager storageManager(){
     return new StorageManager();
 }


}
