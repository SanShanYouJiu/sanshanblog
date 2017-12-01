package com.sanshan.service.auth;

import com.sanshan.dao.mongo.UserRepository;
import com.sanshan.pojo.entity.UserDO;
import com.sanshan.service.SettingService;
import com.sanshan.service.vo.JwtUser;
import com.sanshan.service.vo.ResponseMsgVO;
import com.sanshan.util.setting.Setting;
import com.sanshan.util.info.PosCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

import static java.util.Arrays.asList;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    private AuthenticationManager authenticationManager;
    private UserDetailsService userDetailsService;
    private JwtTokenUtil jwtTokenUtil;
    private UserRepository userRepository;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

     @Autowired
     private SettingService settingService;
     public static final String DefaultAvatar ="/assets/images/defaultUser.png";

    @Autowired
    public AuthServiceImpl(
            AuthenticationManager authenticationManager,
            UserDetailsService userDetailsService,
            JwtTokenUtil jwtTokenUtil,
            UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userRepository = userRepository;
    }

    @Override
    public boolean register(UserDO userToAdd, ResponseMsgVO responseMsgVO) {
        final String username = userToAdd.getUsername();
        if (userRepository.findByUsername(username) != null ) {
            responseMsgVO.buildWithMsgAndStatus(PosCodeEnum.PARAM_ERROR, "该用户名已存在");
            return false;
        }
        if (userRepository.findByEmail(userToAdd.getEmail()) != null) {
            responseMsgVO.buildWithMsgAndStatus(PosCodeEnum.PARAM_ERROR, "该邮箱已存在");
            return false;
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        final String rawPassword = userToAdd.getPassword();
        Setting setting = settingService.getSetting();
        String domain=setting.getDomain();
        userToAdd.setAvatar(domain+DefaultAvatar);
        userToAdd.setPassword(encoder.encode(rawPassword));
        userToAdd.setLastPasswordResetDate(new Date());
        userToAdd.setRoles(asList("ROLE_USER"));
        userRepository.insert(userToAdd);
        log.info("{}:注册成功",username);
        responseMsgVO.buildOK();
        return true;
    }

    /**
     * 判断用户名是否被禁用
     *
     * @param username 用户名
     * @return true被禁用
     */
    public boolean usernameIsDisabled(String username) {
        if (StringUtils.isEmpty(username)) return false;
        Setting setting = settingService.getSetting();
        String[] disabledName = setting.getDisabledUsernames().split(",");
        for (String s : disabledName) {
            if (StringUtils.equalsIgnoreCase(s, username)) {
                return true;
            }
        }
        return false;
    }



    @Override
    public String login(String username, String password) {
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, password);
        // 执行安全检测
        final Authentication authentication;
        final String token;
        try {
            authentication = authenticationManager.authenticate(upToken);
        } catch (BadCredentialsException e) {
            token=null;
            return token;
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Reload password post-security so we can generate token
        //重新加载 生成token
        log.info("用户{}登录", username);
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
         token = jwtTokenUtil.generateToken(userDetails);
        return token;
    }

    @Override
    public String refresh(String oldToken) {
        final String token = oldToken.substring(tokenHead.length());
        String username = jwtTokenUtil.getUsernameFromToken(token);
        JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);
        if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())) {
            return jwtTokenUtil.refreshToken(token);
        }
        return null;
    }
}