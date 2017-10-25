package com.sanshan.web.controller;

import com.sanshan.service.vo.ResponseMsgVO;
import com.sanshan.util.info.PosCodeEnum;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/")
public class  IndexController {

    @RequestMapping()
    public ModelAndView index() {
        return new ModelAndView("index");
    }


    @RequestMapping("error")
    public ResponseMsgVO error(HttpServletRequest request){
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
       return responseMsgVO.buildWithMsgAndStatus(PosCodeEnum.INTER_ERROR, (String) request.getAttribute("errorMessage"));
    }

}