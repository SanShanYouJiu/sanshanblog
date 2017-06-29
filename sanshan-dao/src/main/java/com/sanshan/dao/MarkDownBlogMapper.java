package com.sanshan.dao;

import com.sanshan.pojo.entity.MarkDownBlogDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;


public interface MarkDownBlogMapper extends Mapper<MarkDownBlogDO> {

     @Select(" SELECT  * FROM  markdown_blog WHERE id =#{_parameter}")
     MarkDownBlogDO selectBlogById(long id);

     @Select("SELECT * FROM markdown_blog WHERE tag= #{tag}")
     List<MarkDownBlogDO> queryByTag(MarkDownBlogDO markDownBlogDO);

     @Select("SELECT * FROM markdown_blog WHERE title= #{title}")
     List<MarkDownBlogDO> queryByTitle(MarkDownBlogDO markDownBlogDO);


     @Delete("DELETE FROM markdown_blog WHERE id = #{_parameter} ")
     int deleteById(long id);

}
