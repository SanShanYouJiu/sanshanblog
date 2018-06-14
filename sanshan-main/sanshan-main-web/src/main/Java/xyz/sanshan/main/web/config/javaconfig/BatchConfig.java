package xyz.sanshan.main.web.config.javaconfig;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * 这里使用spring batch内部自带hsql作为job记录器
 * @author sanshan
 * @date 2018-06-10
 */
@Configuration
@EnableBatchProcessing
public class BatchConfig extends DefaultBatchConfigurer {

    @Autowired
    private MongoTemplate mongoTemplate;


    // mongo
    @Bean
    @Scope(value = "prototype")
    public  <T> MongoItemReader<T> mongoItemReader( ){
        MongoItemReader itemReader = new MongoItemReader();
        itemReader.setTemplate(mongoTemplate);
        return itemReader;
    }

    @Bean
    @Scope(value = "prototype")
    public <T> MongoItemWriter<T> mongoItemWriter(){
        MongoItemWriter itemWriter = new MongoItemWriter();
        itemWriter.setTemplate(mongoTemplate);
        return itemWriter;
    }

    //mybatis
    @Bean
    @Scope(value = "prototype")
    public <T> MyBatisPagingItemReader<T> itemReader(SqlSessionFactory sqlSessionFactory){
        MyBatisPagingItemReader<T> itemReader = new MyBatisPagingItemReader<>();
        itemReader.setSqlSessionFactory(sqlSessionFactory);
        itemReader.setPageSize(200);
        itemReader.setQueryId("");
        return itemReader;
    }

    @Bean
    @Scope(value ="prototype")
    public <T>MyBatisBatchItemWriter<T> itemWriter(SqlSessionFactory sqlSessionFactory){
        MyBatisBatchItemWriter<T> itemWriter = new MyBatisBatchItemWriter<>();
        itemWriter.setSqlSessionFactory(sqlSessionFactory);
        itemWriter.setStatementId("");
        return itemWriter;
    }

    //redis

}
