package com.sanshan.DaoTest;

import com.sanshan.dao.MarkDownBlogMapper;
import com.sanshan.dao.UEditorBlogMapper;
import com.sanshan.pojo.entity.MarkDownBlogDO;
import com.sanshan.pojo.entity.UEditorBlogDO;
import com.sanshan.web.config.javaconfig.DBConfig;
import com.sanshan.web.config.javaconfig.MybatisConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DBConfig.class, MybatisConfig.class})
public class DaoTest {
    @Autowired
    MarkDownBlogMapper markDownBlogMapper;

    @Autowired
    UEditorBlogMapper uEditorBlogMapper;

    @Test
    public void test() {
        MarkDownBlogDO markDownBlogDO = new MarkDownBlogDO();
        markDownBlogDO.setTag("ceshi");
        UEditorBlogDO uEditorBlogDO = new UEditorBlogDO();
        uEditorBlogDO.setTag("测试标题");
        List<UEditorBlogDO> uEditorBlogDOS = uEditorBlogMapper.queryByTag(uEditorBlogDO);
        for (int i = 0; i < uEditorBlogDOS.size(); i++) {
            System.out.println("ceshi代码"+uEditorBlogDOS.get(i));
        }
        List<MarkDownBlogDO> list = markDownBlogMapper.queryByTag(markDownBlogDO);
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
    }

    @Test
    public void test2(){
        MarkDownBlogDO markDownBlogDO = markDownBlogMapper.selectBlogById(1);
        System.out.println(markDownBlogDO);
    }

    @Test
    public  void insert(){
        MarkDownBlogDO m = new MarkDownBlogDO();
        m.setId(3);
        m.setUser("ceshi");
        m.setCreated(new Date());
        m.setUpdated(new Date());
        m.setContent("ces");
        m.setTitle("CES");
        m.setTime(new Date());
        markDownBlogMapper.insert(m);
    }

}
