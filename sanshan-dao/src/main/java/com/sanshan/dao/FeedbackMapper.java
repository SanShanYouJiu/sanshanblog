package com.sanshan.dao;

import com.sanshan.pojo.entity.FeedbackDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

public interface FeedbackMapper extends Mapper<FeedbackDO> {

    @Select("SELECT * FROM feedback WHERE id = #{_parameter}")
    FeedbackDO selectById(long id);

    @Insert("Insert INTO feedback(email,opinion,created,updated) values(#{email},#{opinion},#{created},#{updated})")
    int save(FeedbackDO feedbackDO);
}
