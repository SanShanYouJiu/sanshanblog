package xyz.sanshan.main.service.user;

import com.mongodb.WriteResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import xyz.sanshan.common.JwtAuthenticationResponse;
import xyz.sanshan.common.UserContextHandler;
import xyz.sanshan.common.info.CodeTypeEnum;
import xyz.sanshan.common.info.PosCodeEnum;
import xyz.sanshan.common.vo.ResponseMsgVO;
import xyz.sanshan.main.dao.mongo.UserRepository;
import xyz.sanshan.main.pojo.entity.UserDO;
import xyz.sanshan.main.service.MailService;

import java.util.Objects;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author sanshan
 * www.85432173@qq.com
 */
@Service
@Slf4j
public class UserService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailService mailService;



    private static  final  Integer PASSWORD_MIN_LENGTH=6;
    private static  final  Integer PASSWORD_MAX_LENGTH=30;


    public static final String CODE_PREFIX = "sendEmailCode";

    /**
     * 邮箱匹配正则
     */
    private Pattern emailPattern = Pattern.compile("^([a-zA-Z0-9_\\.\\-])+\\@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,4})+$");

    /**
     * 更改密码
     * @param code
     * @param password
     * @param responseMsgVO
     */
    public void changePwd(String code, String password, ResponseMsgVO responseMsgVO) {
        String username=  UserContextHandler.getUsername();


        UserDO userDO = userRepository.findByUsername(username);
        log.info("用户:{}更改密码",userDO.getUsername());
        String key = CODE_PREFIX + CodeTypeEnum.CHANGE_PWD.getValue() + userDO.getEmail();
        String value = redisTemplate.opsForValue().get(key);
        if (!StringUtils.equals(code, value)) {
            responseMsgVO.buildWithMsgAndStatus(PosCodeEnum.PARAM_ERROR, "验证码错误");
            return;
        }

        if (!checkPassWordLegal(password, responseMsgVO)){
            return;
        }

        // 更新到mongo数据库
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        WriteResult result = userRepository.changePassword(username, passwordEncoder.encode(password));
        if (result.getN() == 0) {
            responseMsgVO.buildWithMsgAndStatus(PosCodeEnum.PARAM_ERROR, "更新失败");
            return;
        }
        responseMsgVO.buildOK();
    }


    /**
     * 检测密码合法性
     */
    public Boolean checkPassWordLegal(String password, ResponseMsgVO responseMsgVO) {
        //更新密码
        if (StringUtils.isEmpty(password)) {
            responseMsgVO.buildWithMsgAndStatus(PosCodeEnum.PARAM_ERROR, "密码为空");
            return false;
        }
        if (password.length() < PASSWORD_MIN_LENGTH) {
            responseMsgVO.buildWithMsgAndStatus(PosCodeEnum.PARAM_ERROR, "密码长度太小 小于6位数");
            return false;
        }
        if (password.length() > PASSWORD_MAX_LENGTH) {
            responseMsgVO.buildWithMsgAndStatus(PosCodeEnum.PARAM_ERROR, "密码长度太长 大于30位");
            return false;
        }
        return true;
    }


    /**
     * 发送邮箱验证码
     */
    public void sendEmailCode(Integer type, String email, ResponseMsgVO responseMsgVO) {
        CodeTypeEnum codeType = CodeTypeEnum.of(type);
        //判空
        if (Objects.isNull(codeType)) {
            responseMsgVO.buildWithMsgAndStatus(PosCodeEnum.PARAM_ERROR, "错误的type类型");
            return;
        }
        String tempKey = CODE_PREFIX + codeType.getValue() + email;
        String tempValue = redisTemplate.opsForValue().get(tempKey);
        if (tempValue != null) {
            responseMsgVO.buildWithMsgAndStatus(PosCodeEnum.FREQUENTLY_REQUEST, "操作频繁,请稍后再试");
            return;
        }
        if (!checkSendMail(codeType, tempKey, email, responseMsgVO)){
            return;
        }

        responseMsgVO.buildOK();
    }

    /**
     * 根据类型选择发送邮箱验证码
     */
    public Boolean checkSendMail(CodeTypeEnum codeType, String tempKey, String email, ResponseMsgVO responseMsgVO) {
        try {
            switch (codeType) {
                case REGISTER:
                    mailService.sendRegister(email);
                    responseMsgVO.buildOK();
                    return true;
                case CHANGE_PWD:
                    mailService.sendCode(tempKey, email);
                    responseMsgVO.buildOK();
                    return true;
                default:
                    return false;
            }
        } catch (Exception e) {
            log.error("send  mail:{} fail", email);
            responseMsgVO.buildWithMsgAndStatus(PosCodeEnum.INTER_ERROR, "发送失败");
            return false;
        }
    }


    /**
     * 忘记密码
     * @param email
     * @param token
     * @param responseMsgVO
     */
    public void forgetPassword(String email, String token, ResponseMsgVO responseMsgVO) {
        //获得具体的user对象
        UserDO userDO = userRepository.findByEmail(email);
        log.info("用户:{}忘记密码",userDO.getUsername());
        String key = CODE_PREFIX + CodeTypeEnum.CHANGE_PWD.getValue() + userDO.getEmail();
        String value = redisTemplate.opsForValue().get(key);
        if (!Objects.isNull(value) && value.equals(token)) {
            //通过随机生成字符串 暂时替换为密码
            String tempPwd = randomString(20);
            userRepository.changePassword(userDO.getUsername(), tempPwd);

            //FIXME: 忘记密码功能
            final String loginToken = "no have";
            JwtAuthenticationResponse response = new JwtAuthenticationResponse(loginToken);
            responseMsgVO.buildOKWithData(response);
            return;
        }

        responseMsgVO.buildWithMsgAndStatus(PosCodeEnum.PARAM_ERROR, "验证码错误");
        return;
    }


    /**
     * 其他功能的验证邮箱code
     */
    public Boolean checkEmailToken(String code, ResponseMsgVO resultVO) {
        String email = redisTemplate.opsForValue().get(code);
        if (StringUtils.isEmpty(email)) {
            resultVO.buildWithPosCode(PosCodeEnum.URL_ERROR);
            return false;
        }
        //判断用户状态

        //更新用户 已验证
        redisTemplate.delete(code);
        return true;
    }

    /**
     * 产生一个随机的字符串
     */
    private static String randomString(Integer length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int num = random.nextInt(62);
            buf.append(str.charAt(num));
        }
        return buf.toString();
    }

    /**
     * 查看Email存在
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
     * 验证邮箱是否合法
     */
    public boolean checkEmailLegal(String email, ResponseMsgVO responseMsgVO) {
        Matcher matcher = emailPattern.matcher(email);
        //查看是否为邮箱
        if (!matcher.matches()) {
            responseMsgVO.buildWithPosCode(PosCodeEnum.EMAIL_NOALLOW);
            return false;
        }
        responseMsgVO.buildOK();
        return true;
    }


    /**
     * 注册后的邮箱认证
     */
    public void checkRegisterEmailToken(String token, ResponseMsgVO responseMsgVO) {
        String username=  UserContextHandler.getUsername();

        UserDO userDO = userRepository.findByUsername(username);
        String email = redisTemplate.opsForValue().get(token);
        if (StringUtils.isEmpty(email)) {
            responseMsgVO.buildWithPosCode(PosCodeEnum.URL_ERROR);
            return;
        }
        if (!StringUtils.equals(email, userDO.getEmail())) {
            responseMsgVO.buildWithMsgAndStatus(PosCodeEnum.PARAM_ERROR, "验证码错误");
            return;
        }
        redisTemplate.delete(token);

        responseMsgVO.buildOK();
    }

}
