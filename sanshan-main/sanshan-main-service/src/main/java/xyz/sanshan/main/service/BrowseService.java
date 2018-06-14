package xyz.sanshan.main.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.sanshan.main.service.editor.BlogIdGenerate;

/**
 * 用户浏览
 * @author sanshan <sanshan@maihaoche.com>
 * @date 2018-05-29
 */
@Service
@Slf4j
public class BrowseService {

    @Autowired
    private BlogIdGenerate blogIdGenerate;

    /**
     * 通过BlogId获取浏览数
     * @param blogId
     * @return
     */
    public Long getBlogBrowsesForBlogId(Long blogId){
        return null;
    }

    /**
     * 增加BlogId对应的博客浏览数
     * @param blogId
     */
    public void BlogBrowsesAddForBlogId(Long blogId){

    }


}
