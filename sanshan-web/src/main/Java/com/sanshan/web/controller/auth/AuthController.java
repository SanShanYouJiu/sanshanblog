package com.sanshan.web.controller.auth;

import com.sanshan.pojo.entity.UserDO;
import com.sanshan.service.user.UserService;
import com.sanshan.service.auth.AuthService;
import com.sanshan.service.vo.ResponseMsgVO;
import com.sanshan.util.JwtAuthenticationRequest;
import com.sanshan.util.JwtAuthenticationResponse;
import com.sanshan.util.info.PosCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO createAuthenticationToken(
            JwtAuthenticationRequest authenticationRequest, @RequestParam(name = "codeid") String codeid) throws AuthenticationException {
        ResponseMsgVO msgVO = new ResponseMsgVO();
        //验证码检测
        String codeValue = redisTemplate.opsForValue().get(CodeController.CODE_ID_PREFIX + codeid);
        if (!authenticationRequest.getCode().equalsIgnoreCase(codeValue)) {
            return msgVO.buildWithMsgAndStatus(
                            PosCodeEnum.PARAM_ERROR, "验证码错误");
        }
        final String token = authService.login(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        if (Objects.isNull(token)) {
            msgVO.buildWithMsgAndStatus(PosCodeEnum.PARAM_ERROR, "账户或密码错误");
            return msgVO;
        }
        //检查是否能登陆
        return msgVO.buildOKWithData(new JwtAuthenticationResponse(token));
    }


    @RequestMapping(value = "/register", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO register(UserDO addedUser,
                                  @RequestParam(name = "codeid") String codeid,
                                  @RequestParam(name = "code") String code
    ) {

        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        //验证码检查
        String codeValue = redisTemplate.opsForValue().get(CodeController.CODE_ID_PREFIX + codeid);
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


    /**
     * 刷新token
     *
     * @param request
     * @return
     * @throws AuthenticationException
     */
    @RequestMapping(value = "/refresh", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO refreshAndGetAuthenticationToken(
            HttpServletRequest request) throws AuthenticationException {
        ResponseMsgVO<JwtAuthenticationResponse> msgVO = new ResponseMsgVO<>();
        String token = request.getHeader(tokenHeader);
        String refreshedToken = authService.refresh(token);
        if (refreshedToken == null) {
            return msgVO.buildWithMsgAndStatus(PosCodeEnum.NO_PRIVILEGE, "没有权限操作");
        } else {
            return msgVO.buildOKWithData(new JwtAuthenticationResponse(refreshedToken));
        }
    }


}