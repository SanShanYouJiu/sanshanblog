package xyz.sanshan.main.service.user.cache;

import xyz.sanshan.main.dao.mybatis.MarkDownBlogMapper;
import xyz.sanshan.main.dao.mybatis.UeditorBlogMapper;
import xyz.sanshan.main.service.convent.BlogConvert;
import xyz.sanshan.main.service.convent.MarkDownEditorConvert;
import xyz.sanshan.main.service.convent.UeditorEditorConvert;
import xyz.sanshan.main.service.vo.BlogVO;
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

