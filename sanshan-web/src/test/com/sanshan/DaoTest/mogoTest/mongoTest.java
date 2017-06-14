package com.sanshan.DaoTest.mogoTest;

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
        userDO.setId((long) 1);
        userDO.setUsername("ceshi");
        userDO.setEmail("www.85432173@qq.com");
        userDO.setEnabled(1);
        userDO.setPassword("$2a$10$k2LkEdHwRhsNodj2yBjFyuwHGfFIwzeFNAsjXSeDI6tTKI593SnUC");
        userDO.setRoles(asList("ROLE_USER"));
        userRepository.save(userDO);
        UserDO userDO1 = userRepository.findByUsername("ceshi");
        System.out.println(userDO1);
    }

}
