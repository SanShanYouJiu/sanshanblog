package xyz.sanshan.main.dao.mybatis;

import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;
import xyz.sanshan.main.pojo.entity.MarkDownBlogDO;
import xyz.sanshan.main.pojo.entity.UeditorBlogDO;

import java.util.Date;
import java.util.List;

public interface UeditorBlogMapper extends Mapper<UeditorBlogDO> {


    @Select("SELECT * FROM ueditor_blog WHERE tag= #{tag}")
    List<UeditorBlogDO> queryByTag(String  tag);

    @Select("SELECT * FROM ueditor_blog WHERE title= #{title}")
    List<UeditorBlogDO> queryByTitle(String  title);

    @Select("SELECT id,title,tag,time FROM ueditor_blog WHERE time= #{time}")
    List<MarkDownBlogDO> queryByDate(Date time);

    /**
     * 没有查询content属性 通过id进行间接的去缓存查
     * @param username
     * @return
     */
    @Select("SELECT id,title,tag,user,time FROM ueditor_blog WHERE user= #{username}")
    List<UeditorBlogDO> queryByUser(String  username);


}
