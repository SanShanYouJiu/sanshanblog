package com.sanshan.web.controller.login;

import com.alibaba.fastjson.JSONObject;
import com.sanshan.service.user.UserService;
import com.sanshan.service.vo.ResponseMsgVO;
import com.sanshan.util.info.PosCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@Slf4j
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    RedisTemplate<String,String> redisTemplate;


    @RequestMapping(value = "/login",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO<?> login(String email, String passowrd, String codevalidate
    , HttpSession session, HttpServletResponse response, HttpServletRequest request){
        JSONObject result = new JSONObject();
        ResponseMsgVO<JSONObject> responseMsgVO = new ResponseMsgVO<>();
        if (StringUtils.isEmpty(email)||StringUtils.isEmpty(passowrd)){
            return responseMsgVO.buildWithMsgAndStatus(PosCodeEnum.PARAM_ERROR, "用户名或密码不能为空");
        }
        //验证码验证
        String code = (String) session.getAttribute("codeValidate");
        if (!StringUtils.equalsIgnoreCase(code, codevalidate)) {
            return responseMsgVO.buildWithPosCode(PosCodeEnum.CODE_ERROR);
        }

        //检测是否能登录


        log.info("用户:{}已登录",email);

       return null;
    }

}
