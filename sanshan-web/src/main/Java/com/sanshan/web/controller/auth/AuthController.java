package com.sanshan.web.controller.auth;

import com.sanshan.pojo.entity.UserDO;
import com.sanshan.service.UserService;
import com.sanshan.service.auth.AuthService;
import com.sanshan.service.vo.ResponseMsgVO;
import com.sanshan.util.info.PosCodeEnum;
import com.sanshan.web.config.javaconfig.auxiliary.jwt.JwtAuthenticationRequest;
import com.sanshan.web.config.javaconfig.auxiliary.jwt.JwtAuthenticationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
@RequestMapping("/auth")
public class AuthController {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> createAuthenticationToken(
            JwtAuthenticationRequest authenticationRequest) throws AuthenticationException {
        final String token = authService.login(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        //验证码验证

        // 检查是否能登陆

        // Return the token
        log.info("用户{}已登录", authenticationRequest.getUsername());
        return ResponseEntity.ok(new JwtAuthenticationResponse(token));
    }


    @RequestMapping(value = "/register", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO register(UserDO addedUser
    ) {
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        //合法性检查
       if (!userService.checkPassWordLegal(addedUser.getPassword(), responseMsgVO))
           return responseMsgVO;
        // 是否含有违规字段
        if (authService.usernameIsDisabled(addedUser.getUsername()))
            return responseMsgVO.buildWithMsgAndStatus(PosCodeEnum.USERNAME_NOALLOW, "用户名含有违规字段");
        //注册
        if (!authService.register(addedUser, responseMsgVO))
            return responseMsgVO;
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
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> refreshAndGetAuthenticationToken(
            HttpServletRequest request) throws AuthenticationException {
        String token = request.getHeader(tokenHeader);
        String refreshedToken = authService.refresh(token);
        if (refreshedToken == null) {
            return ResponseEntity.badRequest().body(null);
        } else {
            return ResponseEntity.ok(new JwtAuthenticationResponse(refreshedToken));
        }
    }


}