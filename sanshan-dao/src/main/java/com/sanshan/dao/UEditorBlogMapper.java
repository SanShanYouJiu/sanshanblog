package com.sanshan.dao;

import com.sanshan.pojo.entity.UEditorBlogDO;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface UEditorBlogMapper extends Mapper<UEditorBlogDO> {


    @Select("SELECT * FROM ueditor_blog WHERE tag= #{tag}")
    List<UEditorBlogDO> queryByTag(UEditorBlogDO uEditorBlogDO);

    @Select("SELECT * FROM ueditor_blog WHERE title= #{title}")
    List<UEditorBlogDO> queryByTitle(UEditorBlogDO uEditorBlogDO);


}
