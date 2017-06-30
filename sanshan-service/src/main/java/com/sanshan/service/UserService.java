package com.sanshan.service;

import com.mongodb.WriteResult;
import com.sanshan.dao.mongo.UserRepository;
import com.sanshan.pojo.entity.UserDO;
import com.sanshan.service.vo.JwtUser;
import com.sanshan.service.vo.ResponseMsgVO;
import com.sanshan.util.info.CodeTypeEnum;
import com.sanshan.util.info.PosCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class UserService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private  SettingService settingService;

    public static final String CODE_PREFIX = "sendEmailCode";

    /**
     * 邮箱匹配正则
     */
    private Pattern emailPattern = Pattern.compile("^([a-zA-Z0-9_\\.\\-])+\\@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,4})+$");

    public ResponseMsgVO changePwd(String code, String password) {
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        UserDO userDO = userRepository.findByUsername(jwtUser.getUsername());
        String key = CODE_PREFIX + CodeTypeEnum.CHANGE_PWD.getValue() + userDO.getEmail();
        String value = redisTemplate.opsForValue().get(key);
        if (!StringUtils.equals(code, value)) {
            return new ResponseMsgVO<>().buildWithMsgAndStatus(PosCodeEnum.PARAM_ERROR, "验证码错误");
        }

        if (!checkPassWordLegal(password, responseMsgVO))return responseMsgVO;

        // 更新到mongo数据库
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        WriteResult result = userRepository.changePassword(jwtUser.getUsername(), passwordEncoder.encode(password));
        if (result.getN() == 0)
            return responseMsgVO.buildWithMsgAndStatus(PosCodeEnum.PARAM_ERROR, "更新失败");

        return responseMsgVO.buildOK();
    }


    /**
     * 检测密码合法性
     */
    public Boolean checkPassWordLegal(String password,ResponseMsgVO responseMsgVO){
        //更新密码
        if (StringUtils.isEmpty(password)) {
             responseMsgVO.buildWithMsgAndStatus(PosCodeEnum.PARAM_ERROR, "密码为空");
             return false;
        }
        if (password.length() < 6 ) {
             responseMsgVO.buildWithMsgAndStatus(PosCodeEnum.PARAM_ERROR, "密码长度太小 小于6位数");
             return false;
        }
        if (password.length() > 30){
            responseMsgVO.buildWithMsgAndStatus(PosCodeEnum.PARAM_ERROR, "密码长度太长 大于30位");
            return false;
        }
        return  true;
    }


    /**
     * 发送邮箱验证码
     *
     */
    public ResponseMsgVO sendEmailCode(int type, String email) {
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        CodeTypeEnum codeType = CodeTypeEnum.of(type);
        //判空
        if (Objects.isNull(codeType)) {
            return responseMsgVO.buildWithMsgAndStatus(PosCodeEnum.PARAM_ERROR, "错误的type类型");
        }
        String tempKey = CODE_PREFIX + codeType.getValue() + email;
        String tempValue = redisTemplate.opsForValue().get(tempKey);
        if (tempValue != null) {
            return responseMsgVO.buildWithMsgAndStatus(PosCodeEnum.PARAM_ERROR, "操作频繁,请稍后再试");
        }
       if (!checkSendMail(codeType, tempKey, email, responseMsgVO))return  responseMsgVO;

        return responseMsgVO.buildOK();
    }




    public Boolean checkSendMail(CodeTypeEnum codeType,String tempKey,String email,ResponseMsgVO responseMsgVO){
        try {
            switch (codeType){
                case FIND_PWD:
                    mailService.sendCode(tempKey,email);
                    responseMsgVO.buildOK();return true;
                case REGISTER:
                        mailService.sendRegister(email);
                        responseMsgVO.buildOK();return true;
                case CHANGE_PWD:
                    mailService.sendCode(tempKey, email);
                    responseMsgVO.buildOK();return true;
            }
        } catch (Exception e) {
              log.error("send  mail:{} fail", email);
              responseMsgVO.buildWithMsgAndStatus(PosCodeEnum.INTER_ERROR, "发送失败");return false;
        }
        return false;
    }



    /**
     * 验证邮箱token
     *
     * @param token
     * @param resultVO 统一视图返回对象
     * @return
     */
    public Boolean checkEmailToken(String token, ResponseMsgVO resultVO) {
        String email = redisTemplate.opsForValue().get(token);
        if (StringUtils.isEmpty(email)) {
            resultVO.buildWithPosCode(PosCodeEnum.URL_ERROR);
            return false;
        }
        //判读用户状态

        //更新用户 已验证
        redisTemplate.delete(token);
        return true;
    }


    /**
     *查看Email存在
     */
    public boolean judgeEmail(String email) {
        UserDO userDO = new UserDO();
        if (StringUtils.isNoneEmpty(email)) {
            userDO.setEmail(email);
            userDO = userRepository.findByEmail(email);
        }
        return userDO != null;
    }

    /**
     *验证邮箱是否合法
     */
    public boolean checkEmailLegal(String username,String email,ResponseMsgVO responseMsgVO) {
        Matcher matcher = emailPattern.matcher(email);
        //查看是否为邮箱
        if (!matcher.matches()) {
            responseMsgVO.buildWithPosCode(PosCodeEnum.USERNAME_NOALLOW);
            return false;
        }
        responseMsgVO.buildOK();
        return true;
    }


}
