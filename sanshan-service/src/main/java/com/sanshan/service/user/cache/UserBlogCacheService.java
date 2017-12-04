package com.sanshan.service.user.cache;

import com.sanshan.dao.MarkDownBlogMapper;
import com.sanshan.dao.UeditorBlogMapper;
import com.sanshan.service.convent.BlogConvert;
import com.sanshan.service.convent.MarkDownEditorConvert;
import com.sanshan.service.convent.UeditorEditorConvert;
import com.sanshan.service.vo.BlogVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 */
@Service
@Slf4j
public class UserBlogCacheService {

    @Autowired
    private UeditorBlogMapper uEditorBlogMapper;

    @Autowired
    private MarkDownBlogMapper markDownBlogMapper;


    @Cacheable(value = "spec-user-blogs", key = "'user-blogs:'+#a0")
    public List<BlogVO> getUserBlogs(String username) {
        List<BlogVO> blogVOs1 = BlogConvert.ueditorDoToDtoList(UeditorEditorConvert.doToDtoList(uEditorBlogMapper.queryByUser(username)));
        List<BlogVO> blogVOS2 = BlogConvert.markdownDoToDtoList(MarkDownEditorConvert.doToDtoList(markDownBlogMapper.queryByUser(username)));
        blogVOs1.addAll(blogVOS2);
        return blogVOs1;
    }

    @CachePut(value = "spec-user-blogs", key = "'user-blogs:'+#a0")
    public List<BlogVO> userBlogRefresh(String username) {
        List<BlogVO> blogVOs1 = BlogConvert.ueditorDoToDtoList(UeditorEditorConvert.doToDtoList(uEditorBlogMapper.queryByUser(username)));
        List<BlogVO> blogVOS2 = BlogConvert.markdownDoToDtoList(MarkDownEditorConvert.doToDtoList(markDownBlogMapper.queryByUser(username)));
        blogVOs1.addAll(blogVOS2);
        return blogVOs1;
    }

}

