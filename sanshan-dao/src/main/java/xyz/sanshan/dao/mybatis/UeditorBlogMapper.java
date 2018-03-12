package xyz.sanshan.dao.mybatis;

import xyz.sanshan.pojo.entity.UeditorBlogDO;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface UeditorBlogMapper extends Mapper<UeditorBlogDO> {


    @Select("SELECT * FROM ueditor_blog WHERE tag= #{tag}")
    List<UeditorBlogDO> queryByTag(UeditorBlogDO uEditorBlogDO);

    @Select("SELECT * FROM ueditor_blog WHERE title= #{title}")
    List<UeditorBlogDO> queryByTitle(UeditorBlogDO uEditorBlogDO);


    /**
     * 没有查询content属性 通过id进行间接的去缓存查
     * @param username
     * @return
     */
    @Select("SELECT id,title,tag,user,time FROM ueditor_blog WHERE user= #{username}")
    List<UeditorBlogDO> queryByUser(String  username);


}
