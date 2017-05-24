package com.sanshan.web.config.javaconfig;

import com.baidu.ueditor.upload.StorageManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(value = {"com.sanshan.service"})
public class ServiceConfig {


 @Bean
    public StorageManager storageManager(){
     return new StorageManager();
 }


}
