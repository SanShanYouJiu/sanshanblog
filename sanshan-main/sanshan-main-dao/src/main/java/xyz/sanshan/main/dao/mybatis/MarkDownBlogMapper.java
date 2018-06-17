package xyz.sanshan.main.dao.mybatis;

import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;
import xyz.sanshan.main.pojo.entity.MarkDownBlogDO;

import java.util.Date;
import java.util.List;


public interface MarkDownBlogMapper extends Mapper<MarkDownBlogDO> {

     @Select("SELECT * FROM markdown_blog WHERE tag= #{tag}")
     List<MarkDownBlogDO> queryByTag(String tag);

     @Select("SELECT * FROM markdown_blog WHERE title= #{title}")
     List<MarkDownBlogDO> queryByTitle(String  title);

     @Select("SELECT id,title,tag,time FROM markdown_blog WHERE time= #{time}")
     List<MarkDownBlogDO> queryByDate(Date time);

     /**
      * 没有查询content属性 通过id进行间接的去缓存查
      * @param username
      * @return
      */
     @Select("SELECT id,title,tag,time FROM markdown_blog WHERE user= #{username}")
     List<MarkDownBlogDO> queryByUser(String username);


}
