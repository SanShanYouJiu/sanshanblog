package com.sanshan.DaoTest.ServiceTest;

import com.sanshan.pojo.dto.MarkDownBlogDTO;
import com.sanshan.service.editor.MarkDownBlogService;
import com.sanshan.web.config.javaconfig.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ServiceConfig.class, DBConfig.class,
        MybatisConfig.class, RedisCacheConfig.class, TransactionConfig.class,QuartzConfig.class
,MongoDBConfig.class})
public class BlogServiceTest {


    @Autowired
    MarkDownBlogService markDownBlogService;

    @Test
    public void queryIdTest(){
        MarkDownBlogDTO markDownBlogDTO = markDownBlogService.queryDtoById((long) 1);
        System.out.println(markDownBlogDTO);
    }


}
