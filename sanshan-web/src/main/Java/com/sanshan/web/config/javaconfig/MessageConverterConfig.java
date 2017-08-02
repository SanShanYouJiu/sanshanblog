package com.sanshan.web.config.javaconfig;

import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter4;
import com.alibaba.fastjson.util.IOUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ResourceHttpMessageConverter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 采用的FastJson作为Http转换器
 */
@Configuration
public class MessageConverterConfig {

    @Bean
    public FastJsonHttpMessageConverter4 fastJsonHttpMessageConverter4(FastJsonConfig fastJsonConfig){
        FastJsonHttpMessageConverter4 jsonHttpMessageConverter4 = new FastJsonHttpMessageConverter4();
        List<MediaType> list = new ArrayList();
        list.add(MediaType.APPLICATION_JSON_UTF8);
        jsonHttpMessageConverter4.setSupportedMediaTypes(list);
        jsonHttpMessageConverter4.setFastJsonConfig(fastJsonConfig);
        return jsonHttpMessageConverter4;
    }

    @Bean
    public FastJsonConfig fastJsonConfig(){
        FastJsonConfig config = new FastJsonConfig();
        //其实默认也是UTF-8编码
        config.setCharset(IOUtils.UTF8);
        config.setDateFormat("yyyy-MM-dd HH:mm:ss");
        return config;
    }



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
