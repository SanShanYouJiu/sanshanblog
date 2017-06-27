package com.sanshan.web.controller.auth;

import com.sanshan.pojo.entity.UserDO;
import com.sanshan.service.auth.AuthService;
import com.sanshan.service.vo.ResponseMsgVO;
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
import java.util.regex.Pattern;

@RestController
@Slf4j
@RequestMapping("/auth")
public class AuthController {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private AuthService authService;


    /**
     * 邮箱匹配正则
     */
    private Pattern emailPattern = Pattern.compile("^([a-zA-Z0-9_\\.\\-])+\\@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,4})+$");


    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> createAuthenticationToken(
             JwtAuthenticationRequest authenticationRequest) throws AuthenticationException {
        final String token = authService.login(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        //验证码验证

        // 检查是否能登陆

        // Return the token
        log.info("用户{}已登录",authenticationRequest.getUsername());
        return ResponseEntity.ok(new JwtAuthenticationResponse(token));
    }


    @RequestMapping(value = "/register", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public UserDO register( UserDO addedUser
    ) {
         //合法性检查

        // 是否含有违规字段
        return authService.register(addedUser);
    }



    //TODO 检查邮箱是否存在
    /**
     * 检查邮箱是否存在
     * @param username 要检查的用户名
     */
    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/check", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO checkUsername(String username, String email) {
        return null;
    }


    //todo 实现注册后邮箱验证
    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/register/check/token", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public UserDO registerCheckToken( String token
    ) {

        return null;
    }

    /**
     * 刷新token
     * @param request
     * @return
     * @throws AuthenticationException
     */
    @RequestMapping(value = "/refresh", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
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

    //TODO 忘记密码
    @RequestMapping(value = "/email/send",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO sendEmail(String email){

        return new ResponseMsgVO().buildOKWithData("测试代码");
    }


}