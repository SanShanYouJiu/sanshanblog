package com.sanshan.web.config.javaconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ResourceHttpMessageConverter;

import java.util.LinkedList;
import java.util.List;

/**
 * 采用的FastJson作为Http转换器 由于FastJson版本更新 在WebConfig中集成
 * 另外资源{@link Resource}单独使用Spring内置转换器{@link ResourceHttpMessageConverter}
 */
@Configuration
public class MessageConverterConfig {


    @Bean
    public ResourceHttpMessageConverter  resourceHttpMessageConverter(){
        ResourceHttpMessageConverter res = new ResourceHttpMessageConverter();
        List<org.springframework.http.MediaType> mediaTypes = new LinkedList<MediaType>();
        mediaTypes.add(MediaType.APPLICATION_JSON);
        mediaTypes.add(MediaType.APPLICATION_OCTET_STREAM);
        mediaTypes.add(MediaType.IMAGE_JPEG);
        mediaTypes.add(MediaType.IMAGE_PNG);
        mediaTypes.add(MediaType.IMAGE_GIF);
        mediaTypes.add(MediaType.TEXT_PLAIN);
        res.setSupportedMediaTypes(mediaTypes);
         return res;
    }

}
