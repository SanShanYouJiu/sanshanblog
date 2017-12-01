package com.sanshan.web.config.javaconfig;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import org.springframework.beans.factory.annotation.Value;
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

    //我个人推荐的是采用密码账号进行保护的 但是mongo的docker镜像并不支持 所以暂时注释掉
    @Value("${mongo.hostName}")
    private  String hostName;

    //@Value("${mongo.port}")
    //private int port;
    //
    //@Value("${mongo.username}")
    //private String username;
    //
    //@Value("${mongo.password}")
    //private String  password;

    @Value("${mongo.databaseName}")
    private String  databaseName;


    @Override
    protected String getDatabaseName() {
        return databaseName;
    }

    @Override
    public Mongo mongo() throws Exception {
        //MongoCredential credential = MongoCredential.createCredential(username, databaseName, password.toCharArray());
        return new MongoClient(new ServerAddress(hostName));
    }


    @Bean
    public GridFsTemplate gridFsTemplate() throws Exception {
        GridFsTemplate gridFsTemplate = new GridFsTemplate(mongoDbFactory(),mappingMongoConverter());
           return gridFsTemplate;
    }


}
