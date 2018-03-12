package xyz.sanshan.service;

import xyz.sanshan.common.AuthCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class MailService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private JavaMailSenderImpl javaMailSender;

    @Value("${mail.prefix}")
    private String  mailPrefix;


    private static final AtomicInteger POOL_NUMBER = new AtomicInteger(1);
    private ExecutorService pool =  new ThreadPoolExecutor(0,4,3,TimeUnit.MINUTES,new SynchronousQueue<>(),(r)->{
        Thread t = new Thread(r);
        t.setName("mail-thread:"+POOL_NUMBER);
        return t;
    });

    /**
     * 注册后发送验证邮件
     *
     * @param email 邮箱
     */
    public void sendRegister(String email) {
        String token = DigestUtils.sha256Hex(email + System.currentTimeMillis());
        String text = "您的验证链接为:" + mailPrefix + "?token=" + token;
        String subject = "sanshanblog注册验证";
        sendEmail(email, text, subject);
        redisTemplate.opsForValue().set(token, email, 2L, TimeUnit.HOURS);
    }


    /**
     * 发送验证码
     */
    public void sendCode(String key, String email) {
        String code = AuthCodeUtil.getCode(5);
        String text = "您的验证码是 "+code+" 有效期是5分钟";
        String subject = "sanshanblog验证码";
        sendEmail(email, text, subject);
        redisTemplate.opsForValue().set(key, code, 5, TimeUnit.MINUTES);
    }


    /**
     * 发送邮件
     * @param email   邮箱
     * @param text    内容
     * @param subject 标题
     */
    private void sendEmail(String email, String text, String subject) {
        // 构建简单邮件对象
        SimpleMailMessage smm = new SimpleMailMessage();
        // 设定邮件参数
        smm.setTo(email);
        smm.setFrom(javaMailSender.getUsername());
        smm.setSubject(subject);
        smm.setText(text);
        // 发送邮件
        pool.execute(()->{
            javaMailSender.send(smm);
            log.info("Email:{}发送成功",email);
        });
        log.info("已发送邮件:{}", email);
    }

}
