package xyz.sanshan.service.auth;

import xyz.sanshan.pojo.entity.UserDO;
import xyz.sanshan.service.vo.ResponseMsgVO;

public interface AuthService {
    boolean register(UserDO userToAdd, ResponseMsgVO responseMsgVO);

    boolean usernameIsDisabled(String username);

    String login(String username, String password);

    String refresh(String oldToken);
}

