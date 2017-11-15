package com.sanshan.service;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sanshan.dao.FeedbackMapper;
import com.sanshan.dao.mongo.FileOperation;
import com.sanshan.pojo.entity.FeedbackDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class FeedBackService {

    @Autowired
    private FeedbackMapper feedbackMapper;

    @Autowired
    private FileOperation fileOperation;

    private static final AtomicInteger poolNumber = new AtomicInteger(1);

    private ExecutorService pool = new ThreadPoolExecutor(0, 4,
            3, TimeUnit.MINUTES,
            new SynchronousQueue<Runnable>(),(r)->{
        Thread t = new Thread(r);
        t.setName("feedback-save-thread:"+poolNumber.incrementAndGet());
        return t;
    });


    public void saveBaseInfo(String email, String opinion) {
        Runnable runnable = () -> {
            FeedbackDO feedbackDO = new FeedbackDO();
            feedbackDO.setEmail(email);
            feedbackDO.setOpinion(opinion);
            feedbackDO.setCreated(new Date());
            feedbackDO.setUpdated(new Date());
            feedbackMapper.save(feedbackDO);
            log.info("存入反馈信息已成功");
        };
        pool.execute(runnable);
    }


    public void saveFile(String email, String opinion, MultipartFile multipartFile) {
        Runnable runnable = () -> {
            try {
                DBObject metedata = new BasicDBObject();
                metedata.put("otherEmail", email);
                metedata.put("otherOpinion", opinion);
                fileOperation.saveFile(multipartFile.getInputStream(), "反馈文件:" + multipartFile.getOriginalFilename(), multipartFile.getContentType(), metedata);
                log.info("存入文件:{}成功", multipartFile.getName());
            } catch (IOException e) {
                log.error("存入反馈文件出错");
                e.printStackTrace();
            }
        };
        pool.execute(runnable);
    }

    @Cacheable(value = {"feedback"}, key = "'feedback'+#a0")
    public FeedbackDO get(long id) {
        return feedbackMapper.selectById(id);
    }


}
