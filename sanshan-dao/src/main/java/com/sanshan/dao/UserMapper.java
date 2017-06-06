package com.sanshan.dao;

import com.sanshan.pojo.entity.UserDO;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

public interface UserMapper extends Mapper<UserDO> {
   @Select("SELECT * FROM user where email=#{email} ")
   UserDO findByEmail(String email);
}
