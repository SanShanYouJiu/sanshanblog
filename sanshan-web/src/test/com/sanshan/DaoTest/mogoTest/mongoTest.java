package com.sanshan.DaoTest.mogoTest;

import com.mongodb.WriteResult;
import com.sanshan.dao.mongo.FileOperation;
import com.sanshan.dao.mongo.UserRepository;
import com.sanshan.pojo.entity.UserDO;
import com.sanshan.web.config.javaconfig.MongoDBConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static java.util.Arrays.asList;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MongoDBConfig.class})
public class mongoTest {

    @Autowired
    FileOperation fileOperation;

    @Autowired
    UserRepository userRepository;

    @Test
    public void test(){
        UserDO userDO = new UserDO();
        userDO.setUsername("ceshi32");
        userDO.setEmail("www.85432173@qq.com");
        userDO.setEnabled(1);
        userDO.setPassword("侧视3");
        userDO.setRoles(asList("ROLE_USER"));
        userRepository.save(userDO);
        UserDO userDO1 = userRepository.findByUsername("ceshi32");
        System.out.println(userDO1);
    }

    @Test
    public   void updateTest(){
        WriteResult result = userRepository.changePassword("ceshi2","asdfghjkl");
        System.out.println(result.toString());
    }

}
