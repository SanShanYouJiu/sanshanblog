package com.sanshan.DaoTest.UserService;

import com.sanshan.pojo.dto.UserDTO;
import com.sanshan.service.user.UserService;
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
public class UserTest {

  @Autowired
  private UserService userService;


  @Test
    public void test(){
      UserDTO userDTO = userService.findByEmail("www.85432173@qq.com");
      System.out.println(userDTO);
  }
}
