package com.sanshan.DaoTest;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sanshan.service.editor.MarkDownBlogService;
import com.sanshan.web.config.javaconfig.*;
import com.sanshan.dao.MarkDownBlogMapper;
import com.sanshan.pojo.entity.MarkDownBlogDO;
import com.sanshan.pojo.entity.UEditorBlogDO;
import com.sanshan.service.editor.CacheService.MarkDownBlogCacheService;
import com.sanshan.service.editor.CacheService.UEditorBlogCacheService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ServiceConfig.class, DBConfig.class,
        MybatisConfig.class, RedisCacheConfig.class, TransactionConfig.class,QuartzConfig.class
        ,MongoDBConfig.class})
public class Unifytest {
    @Autowired
    MarkDownBlogCacheService markDownBlogService;

    @Autowired
    MarkDownBlogMapper markDownBlogMapper;
    @Autowired
    UEditorBlogCacheService uEditorBlogService;


    @Test
    public void markdown_blog_test() {
        MarkDownBlogDO blog2 = markDownBlogMapper.selectBlogById((long)1);
        System.out.println(blog2);
    }

    @Test
    public void markdown_blog_query_page() {
        System.out.println("怎么回事");
        MarkDownBlogDO markDownBlog = new MarkDownBlogDO();
        PageHelper.startPage(1, 1);
//        PageInfo<MarkDownBlog> pageInfo = markDownBlogService.queryPageListByWhere(markDownBlog, 1, 2);
        List<MarkDownBlogDO> list = markDownBlogService.queryAll();

        PageInfo info = new PageInfo(list);
        System.out.println("markdownBlogs 的地方"+ info.getList().get(0)+"测试");
    }
    //在不是protected权限保护之前测试
    //@Test
    //public void Markdown_blog_query_one(){
    //    MarkDownBlogDO markDownBlog = new MarkDownBlogDO();
    //    markDownBlog.setUser("ceshi");
    //    MarkDownBlogDO markDownBlog2 = markDownBlogService.queryOne(markDownBlog);
    //    System.out.println(markDownBlog2);
    //}


    @Test
    public void markdwon_blog_query_all(){
        List<MarkDownBlogDO> blogs = markDownBlogService.queryAll();
        for (int i = 0; i <blogs.size() ; i++) {
            System.out.println(blogs.get(i));
        }
    }

    @Test
    public void markdown_blog_test2() {
        MarkDownBlogDO blog = new MarkDownBlogDO();
        blog.setCreated(new Date());
        blog.setUpdated(new Date());
        blog.setContent("# ceshi2");
        blog.setId(2);
        blog.setUser("ceshi");
        blog.setTag("ceshi");
        blog.setTitle("测试标题 字段2");
        blog.setTime(new Date());
        markDownBlogService.save(blog);
    }



    //在不是protected权限保护之前测试
    //@Test
    //public void markdown_blog_test3() {
    //    MarkDownBlogDO blog = new MarkDownBlogDO();
    //    blog.setCreated(new Date());
    //    blog.setUpdated(new Date());
    //    blog.setContent("#ceshi");
    //    blog.setId(3);
    //    blog.setUser("ceshiuSER");
    //    blog.setTag("ceshi222");
    //    blog.setTitle("测试标题");
    //    blog.setTime(new Date());
    //    markDownBlogService.updateSelective(blog);
    //}

    @Test
    public void ueditor_blog_test1(){
        uEditorBlogService.deleteById((long) 4);
    }

    @Test
    public void  ueditor_blog_test2(){
        UEditorBlogDO uEditorBlog = new UEditorBlogDO();
        uEditorBlog.setId(3);
        uEditorBlog.setContent("ueditor 测试1");
        uEditorBlog.setCreated(new Date());
        uEditorBlog.setUpdated(new Date());
        uEditorBlog.setTag("测试标题");
        uEditorBlog.setTime(new Date());
        uEditorBlog.setUser("ceshi");
        uEditorBlogService.save(uEditorBlog);
    }


    @Test
    public  void page_markdown_test(){
        Map<String,Object> maps =new HashMap();
        PageHelper.startPage(2,2);
        List<MarkDownBlogDO> list = markDownBlogService.queryAll();
        PageInfo<MarkDownBlogDO> info = new PageInfo<MarkDownBlogDO>(list);
        maps.put("total", info.getTotal());
        maps.put("rows", list);
        System.out.println("ceshi 代码");
        System.out.println(maps);
    }



    @Test
    public void delete_markdown_blog(){
        markDownBlogMapper.DeleteById((long) 3);

    }




}


