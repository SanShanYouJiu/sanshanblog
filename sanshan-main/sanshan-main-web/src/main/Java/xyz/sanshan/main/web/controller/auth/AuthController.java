package xyz.sanshan.main.web.controller.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import xyz.sanshan.common.info.ConstanceCacheKey;
import xyz.sanshan.common.info.PosCodeEnum;
import xyz.sanshan.common.vo.ResponseMsgVO;
import xyz.sanshan.main.pojo.entity.UserDO;
import xyz.sanshan.main.service.auth.AuthService;
import xyz.sanshan.main.service.user.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;


    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO register(UserDO addedUser,
                                  @RequestParam(name = "codeid") String codeid,
                                  @RequestParam(name = "code") String code
    ) {

        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        //验证码检查
        String codeValue = redisTemplate.opsForValue().get(ConstanceCacheKey.CODE_ID_PREFIX + codeid);
        if (!code.equalsIgnoreCase(codeValue)) {
            return responseMsgVO.buildWithMsgAndStatus(PosCodeEnum.PARAM_ERROR, "验证码错误");
        }

        //合法性检查
        if (!userService.checkPassWordLegal(addedUser.getPassword(), responseMsgVO)){
            return responseMsgVO;
        }
        // 是否含有违规字段
        if (authService.usernameIsDisabled(addedUser.getUsername())){
            return responseMsgVO.buildWithMsgAndStatus(PosCodeEnum.USERNAME_NOALLOW, "用户名含有违规字段");
        }
        //注册
        if (!authService.register(addedUser, responseMsgVO)){
            return responseMsgVO;
        }
        return responseMsgVO;
    }


    @PostMapping(value = "/register-novalidate", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    /**
     * 不需要图片验证码 但是需要对防刷进行控制
     */
    public ResponseMsgVO register(UserDO addedUser
    ) {

        ResponseMsgVO responseMsgVO = new ResponseMsgVO();

        //合法性检查
        if (!userService.checkPassWordLegal(addedUser.getPassword(), responseMsgVO)){
            return responseMsgVO;
        }
        // 是否含有违规字段
        if (authService.usernameIsDisabled(addedUser.getUsername())){
            return responseMsgVO.buildWithMsgAndStatus(PosCodeEnum.USERNAME_NOALLOW, "用户名含有违规字段");
        }
        //注册
        if (!authService.register(addedUser, responseMsgVO)){
            return responseMsgVO;
        }
        return responseMsgVO;
    }



    /**
     * 查看是否拥有登录权限
     * @return
     */
    @GetMapping(value = "/login-status",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public  ResponseMsgVO loginStatus(){
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        responseMsgVO.buildOKWithData("login is ok");
        return responseMsgVO;
    }


}