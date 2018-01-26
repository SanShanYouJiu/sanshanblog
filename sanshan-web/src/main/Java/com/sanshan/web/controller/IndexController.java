package com.sanshan.web.controller;

import com.sanshan.service.vo.ResponseMsgVO;
import com.sanshan.util.info.PosCodeEnum;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/")
public class  IndexController implements ErrorController {

    private static final String PATH = "/error";

    @RequestMapping()
    public ModelAndView index() {
        return new ModelAndView("index");
    }


    @RequestMapping(value = PATH)
    public ResponseMsgVO error(HttpServletRequest request){
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
       return responseMsgVO.buildWithMsgAndStatus(PosCodeEnum.INTER_ERROR, (String) request.getAttribute("errorMessage"));
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }
}