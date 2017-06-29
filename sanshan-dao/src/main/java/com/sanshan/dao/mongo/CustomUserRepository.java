package com.sanshan.dao.mongo;

import com.mongodb.WriteResult;

public interface CustomUserRepository {
    /**
     更改密码
     */
    WriteResult changePassword( final  String username,final  String password);

}
