package com.sanshan.web.controller.auth;

import com.sanshan.dao.mongo.UserRepository;
import com.sanshan.pojo.entity.UserDO;
import com.sanshan.service.UserService;
import com.sanshan.service.vo.ResponseMsgVO;
import com.sanshan.util.info.PosCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.regex.Pattern;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;


    /**
     * 邮箱匹配正则
     */
    private Pattern emailPattern = Pattern.compile("^([a-zA-Z0-9_\\.\\-])+\\@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,4})+$");


    @RequestMapping(value = "/change-pwd",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PreAuthorize("hasRole('USER')")
    public ResponseMsgVO changePassword(@RequestParam(name = "code") String code,
                                        @RequestParam(name = "password") String password){
       return userService.changePwd(code, password);
    }


    //TODO 检查邮箱是否存在
    /**
     * 检查邮箱是否存在
     * @param username 要检查的用户名
     */
    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/email/check", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO checkUsername(String username, String email) {
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        //查看是否存在邮箱
        if (!userService.judgeEmail(email))
              return  responseMsgVO.buildWithPosCode(PosCodeEnum.Email_EXIST);
        //合法性检测
        if (!userService.checkEmailLegal(username,email, responseMsgVO))
             return  responseMsgVO;

        return responseMsgVO;
    }


    //todo 实现注册后邮箱验证
    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/register/check/token", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public UserDO registerCheckToken(@RequestParam(name = "token") String token
    ) {

        return null;
    }


    @RequestMapping(value = "/email/send",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO sendEmailCode(
            @RequestParam(name = "type") int type,
            @RequestParam(name = "email") String email){
        return userService.sendEmailCode(type, email);
    }


    //TODO 验证邮箱token
    /**
     * 验证邮箱token
     * todo验证方式
     * @param token token
     */
    @RequestMapping(value = "/check/token", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO checkToken(@RequestParam(name = "token") String token) {
        ResponseMsgVO responseMsgVO = new ResponseMsgVO<>();
        userService.checkEmailToken(token,responseMsgVO);
        return responseMsgVO.buildOK();
    }


}
