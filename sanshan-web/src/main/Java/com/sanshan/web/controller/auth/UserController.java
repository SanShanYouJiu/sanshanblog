package com.sanshan.web.controller.auth;

import com.sanshan.dao.mongo.UserRepository;
import com.sanshan.service.UserService;
import com.sanshan.service.vo.ResponseMsgVO;
import com.sanshan.util.info.PosCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;


    /**
     *更改密码 在更改密码之前需要发送邮箱验证码
     */
    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/change-pwd",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO changePassword(@RequestParam(name = "code") String code,
                                        @RequestParam(name = "password") String password){
       return userService.changePwd(code, password);
    }


    /**
     * 检查邮箱是否可以使用
     */
    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/email/check", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO checkUsername(@RequestParam(name = "email") String email) {
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        //查看是否存在邮箱
        if (userService.judgeEmail(email))
              return  responseMsgVO.buildWithPosCode(PosCodeEnum.Email_EXIST);
        //合法性检测
        if (!userService.checkEmailLegal(email, responseMsgVO))
             return  responseMsgVO;

        return responseMsgVO;
    }


    /**
     * 注册之后的邮箱认证
     */
    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/register/check/token", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO registerCheckToken(@RequestParam(name = "token") String token
    ) {

      return  userService.checkRegisterEmailToken(token);
    }


    /**
     * 发送邮箱验证码
     * @param type 对应的是CodyTypeEnum类型 1注册 2找回密码 3更改密码 4是忘记密码
     */
    @RequestMapping(value = "/email/send",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO sendEmailCode(
            @RequestParam(name = "type") int type,
            @RequestParam(name = "email") String email){
        return userService.sendEmailCode(type, email);
    }

    /**
     忘记密码
     */
    @RequestMapping(value = "/forget-pwd", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> findPassword(@RequestParam(name = "username") String username,
                                          @RequestParam(name = "token") String token) {
         return  userService.forgetPassword(username, token);
    }


    /**
     * 作为其他功能的 目前暂留
     * 验证邮箱token
     * @param token token
     */
    @RequestMapping(value = "/check/token", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO checkToken(@RequestParam(name = "token") String token) {
        ResponseMsgVO responseMsgVO = new ResponseMsgVO<>();
        userService.checkEmailToken(token,responseMsgVO);
        return responseMsgVO.buildOK();
    }

}
