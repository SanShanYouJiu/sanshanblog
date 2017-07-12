package com.sanshan.service;

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

@Service
@Slf4j
public class FeedBackService {

   @Autowired
 private   FeedbackMapper feedbackMapper;

   @Autowired
   private FileOperation fileOperation;

   public void store(String email, String opinion) {
      FeedbackDO feedbackDO = new FeedbackDO();
      feedbackDO.setEmail(email);
      feedbackDO.setOpinion(opinion);
      feedbackDO.setCreated(new Date());
      feedbackDO.setUpdated(new Date());
      feedbackMapper.save(feedbackDO);
   }

   public void saveFile(String email,String opinion,MultipartFile multipartFile) {
      try {
         fileOperation.saveFile(multipartFile.getInputStream(),email+multipartFile.getOriginalFilename(),multipartFile.getContentType(),"反馈的意见文件");
      } catch (IOException e) {
          log.error("存入反馈文件出错");
         e.printStackTrace();
      }
   }

   @Cacheable(value = {"feedback"},key ="'feedback'+#a0" )
   public FeedbackDO get(long id){
     return feedbackMapper.selectById(id);
   }


}
