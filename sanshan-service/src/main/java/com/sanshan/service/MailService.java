package com.sanshan.service;

import com.sanshan.util.AuthCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class MailService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private JavaMailSenderImpl javaMailSender;

    @Value("${mail.prefix}")
    private String  mailPrefix;

    /**
     * 注册后发送验证邮件
     *
     * @param email 邮箱
     */
    public void sendRegister(String email) {
        String token = DigestUtils.sha256Hex(email + System.currentTimeMillis());
        String text = "您的验证链接为:" + mailPrefix + "token=" + token;
        String subject = "sanshanblog注册验证";
        sendEmail(email, text, subject);
        redisTemplate.opsForValue().set(token, email, 2L, TimeUnit.HOURS);
    }


    /**
     * 发送验证码
     */
    public void sendCode(String key, String email) {
        String code = AuthCodeUtil.getCode(5);
        String text = "您的验证码 "+code+" 有效期是5分钟";
        String subject = "sanshanblog验证码";
        sendEmail(email, text, subject);
        redisTemplate.opsForValue().set(key, code, 5, TimeUnit.MINUTES);
    }

    /**
     * 发送邮件
     *todo 修复邮箱错误
     * @param email   邮箱
     * @param text    内容
     * @param subject 标题
     */
    private void sendEmail(String email, String text, String subject)   {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.163.com");
        props.put("mail.smtp.auth", "true");
        //基本的邮件会话
        Session session = Session.getInstance(props);
        //构造信息体
        MimeMessage message = new MimeMessage(session);
        //发件地址
        try {
        Address address = new InternetAddress("m15261343808@163.com");
        message.setFrom(address);
        //收件地址
        Address toAddress = new InternetAddress(email);
        message.setRecipient(MimeMessage.RecipientType.TO, toAddress);
        //主题
        message.setSubject(subject);
        //正文
        message.setText(text);

        message.saveChanges(); // implicit with send()
        //Exception in thread "main" javax.mail.NoSuchProviderException: smtp
        session.setDebug(true);
        Transport transport = session.getTransport("smtp");

            transport.connect("smtp.163.com", "m15261343808@163.com", "1234567q1996");

        //发送
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        //构建简单邮件对象
        //SimpleMailMessage smm = new SimpleMailMessage();
        // 设定邮件参数
        //smm.setFrom(javaMailSender.getUsername());
        //smm.setTo(email);
        //smm.setSubject(subject);
        //smm.setText(text);
        // 发送邮件
        //javaMailSender.send(smm);
        log.info("发送邮件成功:{}", email);
    }



}
