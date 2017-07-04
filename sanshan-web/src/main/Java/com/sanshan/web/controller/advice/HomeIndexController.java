package com.sanshan.web.controller.advice;

import com.sanshan.dao.mongo.FileOperation;
import com.sanshan.service.vo.ResponseMsgVO;
import com.sanshan.web.config.javaconfig.auxiliary.MultipartFileBucketValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController()
@RequestMapping("/index")
public class HomeIndexController {


    @Autowired
    private MultipartFileBucketValidator multipartFileBucketValidator;

    @Autowired
    private FileOperation fileOperation;


    @RequestMapping(value = "/advice",method = RequestMethod.POST)
    public ResponseMsgVO handleAdvice(@RequestParam(value = "email",required = false)String email, @RequestParam(value = "opinion",required = false)String opinion){
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        return responseMsgVO;
    }

    @InitBinder("multipartFileBucket")
    protected void initBinderMultipartFileBucket(WebDataBinder binder) {
        binder.setValidator(multipartFileBucketValidator);
    }


    @RequestMapping(value = "/advice/upload",method = RequestMethod.POST)
    public ResponseMsgVO handleFileUpload(@Valid MultipartFileBucket multipartFileBucket, BindingResult result){
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        if (result.hasErrors()) {

        }
        return responseMsgVO;
    }


}
