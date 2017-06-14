package com.sanshan.DaoTest.FastJsonTest;

import com.alibaba.fastjson.JSON;
import com.sanshan.service.vo.JwtUser;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

public class JsonConfigTest {

    private JwtUser jwtUser;

    @Test
    public void test() {
        jwtUser = new JwtUser((long) 1,"ceshi","ceshi","www.85432173@qq.com",new ArrayList<>(),new Date());
        String json = JSON.toJSONString(jwtUser);
        System.out.println(json);
    }
}
