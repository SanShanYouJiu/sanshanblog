package com.sanshan.web.config.javaconfig;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.sanshan.dao.mongo")
@ComponentScan(basePackages = "com.sanshan.dao.mongo")
public class MongoDBConfig extends AbstractMongoConfiguration {

    protected String getDatabaseName() {
        return "sanshanblog";
    }


    public Mongo mongo() throws Exception {
        return new MongoClient();
    }

    @Bean
    public GridFsTemplate gridFsTemplate() throws Exception {
        GridFsTemplate gridFsTemplate = new GridFsTemplate(mongoDbFactory(),mappingMongoConverter());
           return gridFsTemplate;
    }


}
