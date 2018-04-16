package xyz.sanshan.auth.security.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import xyz.sanshan.auth.security.server.service.AuthService;
import xyz.sanshan.auth.security.server.util.user.JwtAuthenticationRequest;
import xyz.sanshan.auth.security.server.util.user.JwtAuthenticationResponse;
import xyz.sanshan.common.info.ConstanceCacheKey;
import xyz.sanshan.common.info.PosCodeEnum;
import xyz.sanshan.common.vo.ResponseMsgVO;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("jwt")
public class AuthController {

    @Value("${jwt.token-header}")
    private String tokenHeader;

    @Autowired
    private AuthService authService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @PostMapping(value = "token",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO createAuthenticationToken( JwtAuthenticationRequest authenticationRequest) throws Exception {
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        String codeKey =ConstanceCacheKey.CODE_ID_PREFIX+authenticationRequest.getCodeid();
        String codeValue =  redisTemplate.opsForValue().get(codeKey);
        if (!authenticationRequest.getCode().equalsIgnoreCase(codeValue)) {
           //验证码错误
            return responseMsgVO.buildWithMsgAndStatus(PosCodeEnum.PARAM_ERROR, "验证码错误");
        }
        final String token = authService.login(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        //登录失败
        if (token==null){
            return responseMsgVO.buildWithMsgAndStatus(PosCodeEnum.PARAM_ERROR,"用户名或密码错误");
        }
        return responseMsgVO.buildOKWithData(new JwtAuthenticationResponse(token));
    }

    @GetMapping(value = "refresh",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO refreshAndGetAuthenticationToken(
            HttpServletRequest request) {
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
