package com.sanshan.service.auth;

import com.sanshan.pojo.entity.UserDO;

public interface AuthService {
    UserDO register(UserDO userToAdd);
    String login(String username, String password);
    String refresh(String oldToken);
}

