package com.sanshan.service.editor.CacheService;

import com.github.pagehelper.PageInfo;
import com.sanshan.pojo.entity.MarkDownBlogDO;
import com.sanshan.service.BaseServiceImpl;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *  DO存到缓存中
 */
@Service
public class MarkDownBlogCacheService extends BaseServiceImpl<MarkDownBlogDO> {


    @CachePut(value = {"markdown-blog"})
    @Override
    public List<MarkDownBlogDO> queryAll() {
        return super.queryAll();
    }



    @Cacheable(value = {"markdown-blog"})
    @Override
    public List<MarkDownBlogDO> queryListByWhere(MarkDownBlogDO example) {
        return super.queryListByWhere(example);
    }



    @Cacheable(value = {"markdown-blog"},key = "#a0")
    @Override
    public MarkDownBlogDO queryById(Long id) {
        return super.queryById(id);
    }



    @Cacheable(value = {"markdown-blog"})
    @Override
    public PageInfo<MarkDownBlogDO> queryPageListByWhere(MarkDownBlogDO example, Integer page, Integer rows) {
        return super.queryPageListByWhere(example, page, rows);
    }



    @Override
    public Integer save(MarkDownBlogDO markDownBlog) {
        return super.save(markDownBlog);
    }


    @CacheEvict(value = {"markdown-blog"}, key = "#a0",beforeInvocation = true)
    @Override
    public Integer deleteById(Long id) {
        return super.deleteById(id);
    }


}
