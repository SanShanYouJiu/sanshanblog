package xyz.sanshan.auth.security.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
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

    @Value("${jwt.tokenHead}")
    private String tokenHeader;

    @Autowired
    private AuthService authService;

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping(value = "token", method = RequestMethod.POST)
    public ResponseMsgVO createAuthenticationToken(
            @RequestBody JwtAuthenticationRequest authenticationRequest) throws Exception {
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        String codeValue = (String) redisTemplate.opsForValue().get(ConstanceCacheKey.CODE_ID_PREFIX + authenticationRequest.getCodeid());
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

    @RequestMapping(value = "refresh", method = RequestMethod.GET)
    public ResponseMsgVO refreshAndGetAuthenticationToken(
            HttpServletRequest request) {
        ResponseMsgVO<xyz.sanshan.common.JwtAuthenticationResponse> msgVO = new ResponseMsgVO<>();
        String token = request.getHeader(tokenHeader);
        String refreshedToken = authService.refresh(token);
        if (refreshedToken == null) {
            return msgVO.buildWithMsgAndStatus(PosCodeEnum.NO_PRIVILEGE, "没有权限操作");
        } else {
            return msgVO.buildOKWithData(new xyz.sanshan.common.JwtAuthenticationResponse(refreshedToken));
        }
    }
}
