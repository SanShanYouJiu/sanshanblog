package com.sanshan.web.config.javaconfig;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@ImportResource("classpath:TransactionConfig.xml")
public class TransactionConfig {

    @Autowired
    private DruidDataSource dataSource;

    @Bean
     public DataSourceTransactionManager transactionManager( ){
         DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
         dataSourceTransactionManager.setDataSource(dataSource);
         return dataSourceTransactionManager;
     }


}
