package com.sanshan.service.editor.CacheService;

import com.github.pagehelper.PageInfo;
import com.sanshan.dao.MarkDownBlogMapper;
import com.sanshan.pojo.entity.MarkDownBlogDO;
import com.sanshan.service.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * DO存到缓存中
 */
@Service
public class MarkDownBlogCacheService extends BaseServiceImpl<MarkDownBlogDO> {


    @Autowired
    MarkDownBlogMapper markDownBlogMapper;

    @Override
    public List<MarkDownBlogDO> queryAll() {
        return super.queryAll();
    }


    @Cacheable(value = {"markdown-blog"}, key = "'markdown'+#a0.tag", condition = "#a0.tag!=null")
    public List<MarkDownBlogDO> queryByTag(MarkDownBlogDO example) {
        return markDownBlogMapper.queryByTag(example);
    }



    @Cacheable(value = {"markdown-blog"}, key = "'markdown'+#a0.title", condition = "#a0.title!=null")
    public List<MarkDownBlogDO> queryByTitle(MarkDownBlogDO markDownBlogDO) {
        return markDownBlogMapper.queryByTitle(markDownBlogDO);
    }


    @Override
    public List<MarkDownBlogDO> queryListByWhere(MarkDownBlogDO example) {
        return super.queryListByWhere(example);
    }


    @Cacheable(value = {"markdown-blog"}, key = "#a0")
    @Override
    public MarkDownBlogDO queryById(Long id) {
        return super.queryById(id);
    }


    @Caching(
            cacheable = {
                    @Cacheable(value = {"markdown-blog"}, key = "#a0.id", condition = "#a0.id!=null"),
                    @Cacheable(value = {"markdown-blog"}, key = "'markdown'+#a0.title", condition = "#a0.title!=null"),
                    @Cacheable(value = {"markdown-blog"}, key = "'markdown'+a0.tag", condition = "#a0.tag!=null"),
                    @Cacheable(value = {"markdown-blog"}, key = "'markdown'+a0.time", condition = "#a0.time!=null"),
            }
    )
    @Override
    public MarkDownBlogDO queryOne(MarkDownBlogDO example) {
        return super.queryOne(example);
    }

    @Override
    public PageInfo<MarkDownBlogDO> queryPageListByWhere(MarkDownBlogDO example, Integer page, Integer rows) {
        return super.queryPageListByWhere(example, page, rows);
    }


    @Override
    public Integer save(MarkDownBlogDO markDownBlog) {
        return super.save(markDownBlog);
    }


    @CacheEvict(value = {"markdown-blog"}, key = "#a0", beforeInvocation = true)
    @Override
    public Integer deleteById(Long id) {
        return super.deleteById(id);
    }


    @Caching(
            put = {
                    @CachePut(value = {"markdown-blog"}, key = "#a0.id", condition = "#a0.id!=null"),
                    @CachePut(value = {"markdown-blog"}, key = "'markdown'+#a0.title", condition = "#a0.title!=null"),
                    @CachePut(value = {"markdown-blog"}, key = "'markdown'+#a0.tag", condition = "#a0.tag!=null"),
                    @CachePut(value = {"markdown-blog"}, key = "'markdown'+#a0.time", condition = "#a0.time!=null")
            }
    )
    @Override
    public MarkDownBlogDO update(MarkDownBlogDO markDownBlogDO) {
        return super.update(markDownBlogDO);
    }


    @Caching(
            put = {
                    @CachePut(value = {"markdown-blog"}, key = "#a0.id", condition = "#a0.id!=null"),
                    @CachePut(value = {"markdown-blog"}, key = "'markdown'+#a0.title", condition = "#a0.title!=null"),
                    @CachePut(value = {"markdown-blog"}, key = "'markdown'+#a0.tag", condition = "#a0.tag!=null"),
                    @CachePut(value = {"markdown-blog"}, key = "'markdown'+#a0.time", condition = "#a0.time!=null")
            }
    )
    public MarkDownBlogDO updateSelective(MarkDownBlogDO markDownBlogDO) {
        super.updateSelective(markDownBlogDO);
        //cache注解是通过切面实现的 调用同一类中的方法不会用到缓存 直接访问数据库获取
        return queryById(markDownBlogDO.getId());
    }
}
