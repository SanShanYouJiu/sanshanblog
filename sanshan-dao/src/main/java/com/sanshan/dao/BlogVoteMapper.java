package com.sanshan.dao;

import com.sanshan.pojo.entity.BlogVoteDO;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

/**
 */
public interface BlogVoteMapper extends Mapper<BlogVoteDO>{

    @Update("update blog_vote set favours =favours+1 where blog_id=#{blogId}")
    int incrFavours(long blogId);

    @Update("update blog_vote set treads =treads+1 where blog_id=#{blogId}")
    int incrTreads(long blogId);
}
