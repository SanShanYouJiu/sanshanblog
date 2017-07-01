package com.sanshan.DaoTest.ServiceTest;

import com.sanshan.web.config.javaconfig.ServiceConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ServiceConfig.class})
public class MailServiceTest {

    @Autowired
    private JavaMailSenderImpl sender;

    @Test
    public void test(){
        String from = "m15261343808@163.com";
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setText("测试代码");
        simpleMailMessage.setSubject("无名者");
        simpleMailMessage.setFrom(from);
        simpleMailMessage.setTo("sanshanyoujiu@gmail.com");
        sender.send(simpleMailMessage);
        System.out.println("发送完成");
    }

}

