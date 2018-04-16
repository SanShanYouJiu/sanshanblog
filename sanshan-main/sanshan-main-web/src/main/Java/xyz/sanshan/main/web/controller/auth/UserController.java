package xyz.sanshan.main.web.controller.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.sanshan.common.info.ConstanceCacheKey;
import xyz.sanshan.common.info.PosCodeEnum;
import xyz.sanshan.common.vo.ResponseMsgVO;
import xyz.sanshan.main.service.user.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;


    /**
     *更改密码 在更改密码之前需要发送邮箱验证码
     */
    @PostMapping(value = "/change-pwd",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO changePassword(@RequestParam(name = "code") String code,
                                        @RequestParam(name = "password") String password){
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        userService.changePwd(code, password, responseMsgVO);
        return responseMsgVO;
    }


    /**
     * 检查邮箱是否可以使用
     */
    @PostMapping(value = "/email/check", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO checkUsername(@RequestParam(name = "email") String email) {
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        //查看是否存在邮箱
        if (userService.judgeEmail(email)){
            return  responseMsgVO.buildWithPosCode(PosCodeEnum.Email_EXIST);
        }
        //合法性检测
        if (!userService.checkEmailLegal(email, responseMsgVO)) {
            return responseMsgVO;
        }

            return responseMsgVO;
        }


    /**
     * 注册之后的邮箱认证
     */
    @PostMapping(value = "/register/check/token",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO registerCheckToken(@RequestParam(name = "token") String token
    ) {
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        userService.checkRegisterEmailToken(token, responseMsgVO);
        return responseMsgVO;
    }


    /**
     * 发送邮箱验证码
     *
     * @param type 对应的是CodyTypeEnum类型 1注册 2更改密码
     */
    @PostMapping(value = "/email/send", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO sendEmailCode(
            @RequestParam(name = "type") Integer type,
            @RequestParam(name = "email") String email,
            @RequestParam(name = "codeid")String codeid,
            @RequestParam(name = "code") String code) {
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
          //验证码
        String codeValidate = redisTemplate.opsForValue().get(ConstanceCacheKey.CODE_ID_PREFIX + codeid);
        if (!code.equalsIgnoreCase(codeValidate)) {
            return new ResponseMsgVO().buildWithMsgAndStatus(PosCodeEnum.PARAM_ERROR, "验证码错误");
        }
        userService.sendEmailCode(type, email, responseMsgVO);
        return responseMsgVO;
    }

    /**
     忘记密码
     */
    @PostMapping(value = "/forget-pwd", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO<?> findPassword(@RequestParam(name = "email") String email,
                                          @RequestParam(name = "code") String code) {
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        userService.forgetPassword(email, code, responseMsgVO);
        return responseMsgVO;
    }



    /**
     * 作为其他功能的 目前暂留
     * 验证邮箱code
     * @param code code
     */
    @PostMapping(value = "/check/code", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO checkToken(@RequestParam(name = "code") String code) {
        ResponseMsgVO responseMsgVO = new ResponseMsgVO<>();
        userService.checkEmailToken(code,responseMsgVO);
        return responseMsgVO.buildOK();
    }

}
