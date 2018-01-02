package com.sanshan.dao;

import com.sanshan.pojo.entity.UeditorIdFileMapDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 */
public interface UeditorIdFileMapMapper extends Mapper<UeditorIdFileMapDO> {

    @Delete("delete from ueditor_id_file_map  where blog_id =#{blog_id}")
    int deleteById(@Param("blog_id") Long blog_id);

    @Select("select  filename from ueditor_id_file_map where blog_id =#{blog_id}")
    List<String> queryFileNamesByBlogId(@Param("blog_id")Long blogId);

    @Select("SELECT  DISTINCT(blog_id) FROM  ueditor_id_file_map")
    List<Long>  queryByAllBlogId();
}
