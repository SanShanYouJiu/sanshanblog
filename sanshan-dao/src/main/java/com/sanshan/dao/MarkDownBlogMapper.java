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

     /**
      * 没有查询content属性 通过id进行间接的去缓存查
      * @param username
      * @return
      */
     @Select("SELECT id,title,tag,time FROM markdown_blog WHERE user= #{username}")
     List<MarkDownBlogDO> queryByUser(String username);


     @Delete("DELETE FROM markdown_blog WHERE id = #{_parameter} ")
     int deleteById(long id);



}
