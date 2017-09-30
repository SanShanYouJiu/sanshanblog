package com.sanshan.web.controller.index;

import com.sanshan.dao.mongo.FileOperation;
import com.sanshan.service.FeedBackService;
import com.sanshan.service.vo.ResponseMsgVO;
import com.sanshan.web.config.javaconfig.auxiliary.MultipartFileBucketValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@Controller()
@RequestMapping("/index")
public class HomeIndexController {


    @Autowired
    private MultipartFileBucketValidator multipartFileBucketValidator;

    @Autowired
    private FileOperation fileOperation;

    @Autowired
    private FeedBackService feedBackService;

    //TODO 作校验使用
    @InitBinder("multipartFileBucket")
    protected void initBinderMultipartFileBucket(WebDataBinder binder) {
        binder.setValidator(multipartFileBucketValidator);
    }


    @RequestMapping(value = "/advice",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseMsgVO handleAdvice(@RequestParam(value = "email")String email, @RequestParam(value = "opinion",required = false)String opinion){
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        responseMsgVO.buildOK();
        feedBackService.store(email,opinion);
        return responseMsgVO;
    }


    @RequestMapping(value = "/advice/file",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseMsgVO handleFileUpload2(
            HttpServletRequest request, @RequestParam(name = "file") MultipartFile multipartFile){
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        String email = request.getHeader("email");
        String opinion = request.getHeader("opinion");
        feedBackService.store(email,"反馈文件:"+multipartFile.getOriginalFilename());
        feedBackService.saveFile(email,opinion,multipartFile);
        return responseMsgVO.buildOK();
    }

}
