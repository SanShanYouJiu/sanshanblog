package xyz.sanshan.main.service.auth;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import xyz.sanshan.common.info.PosCodeEnum;
import xyz.sanshan.common.setting.Setting;
import xyz.sanshan.common.vo.ResponseMsgVO;
import xyz.sanshan.main.api.vo.user.UserInfo;
import xyz.sanshan.main.dao.mongo.UserRepository;
import xyz.sanshan.main.pojo.dto.UserDTO;
import xyz.sanshan.main.pojo.entity.UserDO;
import xyz.sanshan.main.service.SettingService;
import xyz.sanshan.main.service.convent.UserConvert;
import xyz.sanshan.main.service.search.ElasticSearchService;

import java.util.Date;

import static java.util.Arrays.asList;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    private UserRepository userRepository;


    @Autowired
    private ElasticSearchService elasticSearchService;

     @Autowired
     private SettingService settingService;
     public static final String DEFAULT_AVATAR ="/assets/images/defaultUser.png";

    @Autowired
    public AuthServiceImpl(
            UserRepository userRepository) {
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
        userToAdd.setAvatar(domain+DEFAULT_AVATAR);
        userToAdd.setPassword(encoder.encode(rawPassword));
        userToAdd.setLastPasswordResetDate(new Date());
        userToAdd.setRoles(asList("ROLE_USER"));
        userAdd(userToAdd);
        log.info("{}:注册成功",username);
        responseMsgVO.buildOK();
        return true;
    }

    /**
     * 用户增加
     * 将用户信息分发到各个模块上
     * @param userToAdd
     */
    private void  userAdd(UserDO userToAdd){
        UserDO userDO = userRepository.insert(userToAdd);
        //转换为DTO对象 加入到ElasticSearch中
        UserDTO userDTO = UserConvert.doToDto(userDO);
        elasticSearchService.userAdd(userDTO);
    }

    /**
     * 判断用户名是否被禁用
     *
     * @param username 用户名
     * @return true被禁用
     */
    @Override
    public boolean usernameIsDisabled(String username) {
        if (StringUtils.isEmpty(username)){
            return false;
        }
        Setting setting = settingService.getSetting();
        String[] disabledName = setting.getDisabledUsernames().split(",");
        for (String s : disabledName) {
            if (StringUtils.equalsIgnoreCase(s, username)) {
                return true;
            }
        }
        return false;
    }




    public UserInfo validate(String username,String password){
        UserInfo info =null;
        UserDO userDO = userRepository.findByUsername(username);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Boolean validate = encoder.matches(password, userDO.getPassword());
        if (validate){
            info = new UserInfo();
            BeanUtils.copyProperties(userDO, info);
            return info;
        }else {
            return info;
        }
    }

}