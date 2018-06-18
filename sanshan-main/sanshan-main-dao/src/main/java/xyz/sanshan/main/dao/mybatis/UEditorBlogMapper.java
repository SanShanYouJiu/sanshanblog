package xyz.sanshan.main.dao.mybatis;

import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;
import xyz.sanshan.main.pojo.entity.UEditorBlogDO;

import java.util.Date;
import java.util.List;

public interface UEditorBlogMapper extends Mapper<UEditorBlogDO> {


    @Select("SELECT * FROM ueditor_blog WHERE tag= #{tag}")
    List<UEditorBlogDO> queryByTag(String  tag);

    @Select("SELECT * FROM ueditor_blog WHERE title= #{title}")
    List<UEditorBlogDO> queryByTitle(String  title);

    @Select("SELECT id,title,tag,time FROM ueditor_blog WHERE time= #{time}")
    List<UEditorBlogDO> queryByDate(Date time);

    /**
     * 没有查询content属性 通过id进行间接的去缓存查
     * @param username
     * @return
     */
    @Select("SELECT id,title,tag,user,time FROM ueditor_blog WHERE user= #{username}")
    List<UEditorBlogDO> queryByUser(String  username);


}
