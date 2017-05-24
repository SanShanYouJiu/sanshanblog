package com.sanshan.dao;

import com.sanshan.pojo.entity.MarkDownBlogDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;


public interface MarkDownBlogMapper extends Mapper<MarkDownBlogDO> {

     @Select(" SELECT  * FROM  markdown_blog WHERE id =#{_parameter}")
     MarkDownBlogDO selectBlogById(long id);

     @Delete(" DELETE FROM markdown_blog WHERE id = #{_parameter} ")
     void DeleteById(long id);
}
