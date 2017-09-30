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


    /**
     * 没有查询content属性 通过id进行间接的去缓存查
     * @param username
     * @return
     */
    @Select("SELECT id,title,tag,user,time FROM ueditor_blog WHERE user= #{username}")
    List<UEditorBlogDO> queryByUser(String  username);


}
