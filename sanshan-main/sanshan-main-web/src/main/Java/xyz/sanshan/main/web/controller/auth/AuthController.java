package xyz.sanshan.main.web.controller.auth;

import xyz.sanshan.main.api.vo.user.UserInfo;
import xyz.sanshan.main.pojo.entity.UserDO;
import xyz.sanshan.main.service.user.UserService;
import xyz.sanshan.main.service.auth.AuthService;
import xyz.sanshan.common.vo.ResponseMsgVO;
import xyz.sanshan.common.JwtAuthenticationRequest;
import xyz.sanshan.common.JwtAuthenticationResponse;
import xyz.sanshan.common.info.PosCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@RestController
@RequestMapping("/auth")
public class AuthController {
    public static void main(String[] args) {

    }

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
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

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
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
     * 刷新token
     *
     * @param request
     * @return
     * @throws AuthenticationException
     */
    @PostMapping(value = "/refresh-token", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
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

    @PostMapping(value = "/user/validate",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public  ResponseMsgVO userValidate(@RequestParam(name = "username")String username,@RequestParam("password")String password){
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        UserInfo userInfo = authService.validate(username, password);
          if (userInfo!=null){
              responseMsgVO.buildOKWithData(userInfo);
          }else {
              responseMsgVO.buildWithPosCode(PosCodeEnum.PARAM_ERROR);
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