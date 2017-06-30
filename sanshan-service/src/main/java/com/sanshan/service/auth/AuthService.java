package com.sanshan.service.auth;

import com.sanshan.pojo.entity.UserDO;
import com.sanshan.service.vo.ResponseMsgVO;

public interface AuthService {
    boolean register(UserDO userToAdd, ResponseMsgVO responseMsgVO);

    boolean usernameIsDisabled(String username);

    String login(String username, String password);

    String refresh(String oldToken);
}

