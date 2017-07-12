package com.sanshan.web.controller.index;

import com.sanshan.service.FeedBackService;
import com.sanshan.service.vo.ResponseMsgVO;
import com.sanshan.util.info.PosCodeEnum;
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
    private FeedBackService feedBackService;

    @InitBinder("multipartFileBucket")
    protected void initBinderMultipartFileBucket(WebDataBinder binder) {
        binder.setValidator(multipartFileBucketValidator);
    }


    @RequestMapping(value = "/advice",method = RequestMethod.POST)
    public ResponseMsgVO handleAdvice(@RequestParam(value = "email")String email, @RequestParam(value = "opinion",required = false)String opinion){
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        responseMsgVO.buildOK();
        feedBackService.store(email,opinion);
        return responseMsgVO;
    }



    @RequestMapping(value = "/advice/file",method = RequestMethod.POST)
    public ResponseMsgVO handleFileUpload(@RequestParam(value = "email")String email,@RequestParam(value = "opinion")String opinion,@RequestParam(value = "file")@Valid MultipartFileBucket multipartFileBucket, BindingResult result){
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        if (result.hasErrors()) {
            return responseMsgVO.buildWithPosCode(PosCodeEnum.INTER_ERROR);
        }

        feedBackService.saveFile(email,opinion,multipartFileBucket.getMultipartFile());
        return responseMsgVO.buildOK();
    }

    @RequestMapping(value = "/advice/file2",method = RequestMethod.POST)
    public ResponseMsgVO handleFileUpload2(@RequestParam(value = "file")@Valid MultipartFileBucket multipartFileBucket, BindingResult result){
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        if (result.hasErrors()) {
            return responseMsgVO.buildWithPosCode(PosCodeEnum.INTER_ERROR);
        }

        return responseMsgVO.buildOK();
    }



}
