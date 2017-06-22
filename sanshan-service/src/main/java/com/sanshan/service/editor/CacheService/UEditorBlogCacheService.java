package com.sanshan.service.editor.CacheService;

import com.github.pagehelper.PageInfo;
import com.sanshan.dao.UEditorBlogMapper;
import com.sanshan.pojo.entity.UEditorBlogDO;
import com.sanshan.service.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * DO存到缓存中
 */
@Service
public class UEditorBlogCacheService extends BaseServiceImpl<UEditorBlogDO> {

    @Override
    public List<UEditorBlogDO> queryAll() {
        return super.queryAll();
    }

    @Autowired
    UEditorBlogMapper uEditorBlogMapper;


    @Cacheable(value = {"ueditor-blog"},key = "'ueditor'+#a0.tag", condition = "#a0.tag!=null")
    public List<UEditorBlogDO> queryByTag(UEditorBlogDO example) {
        return uEditorBlogMapper.queryByTag(example);
    }

    @Cacheable(value = {"ueditor-blog"},key = "'ueditor'+#a0.title",condition = "#a0.title!=null")
    public List<UEditorBlogDO> queryByTitle(UEditorBlogDO example){
        return uEditorBlogMapper.queryByTitle(example);
    }

    @Override
    public List<UEditorBlogDO> queryListByWhere(UEditorBlogDO example) {
        return super.queryListByWhere(example);
    }

    @Cacheable(value = {"ueditor-blog"},key = "#a0")
    @Override
    public UEditorBlogDO queryById(Long id) {
        return super.queryById(id);
    }


   @Caching(
           cacheable = {
                   @Cacheable(value = {"ueditor-blog"},key = "#a0.id",condition = "#a0.id!=null"),
                   @Cacheable(value = {"ueditor-blog"},key = "'ueditor'+#a0.title",condition = "#a0.title!=null"),
                   @Cacheable(value = {"ueditor-blog"},key = "'ueditor'+#a0.tag",condition = "#a0.tag!=null"),
                   @Cacheable(value = {"ueditor-blog"},key = "'ueditor'+#a0.time",condition = "#a0.time!=null")
           }
   )
    @Override
    public UEditorBlogDO queryOne(UEditorBlogDO example) {
        return super.queryOne(example);
    }


    @Override
    public PageInfo<UEditorBlogDO> queryPageListByWhere(UEditorBlogDO example, Integer page, Integer rows) {
        return super.queryPageListByWhere(example, page, rows);
    }


    @Override
    public Integer save(UEditorBlogDO uEditorBlog) {
        return super.save(uEditorBlog);
    }


    @CacheEvict(value = {"ueditor-blog"}, key = "#a0",beforeInvocation = true)
    @Override
    public Integer deleteById(Long id) {
        return super.deleteById(id);
    }


    @Caching(
            put = {
                    @CachePut(value = {"ueditor-blog"},key = "#a0.id",condition = "#a0.id!=null"),
                    @CachePut(value = {"ueditor-blog"},key = "'ueditor'+#a0.title",condition = "#a0.title!=null"),
                    @CachePut(value = {"ueditor-blog"},key = "'ueditor'+#a0.tag",condition = "#a0.tag!=null"),
                    @CachePut(value = {"ueditor-blog"},key = "'ueditor'+#a0.time",condition = "#a0.time!=null")
            }
    )
    @Override
    public UEditorBlogDO update(UEditorBlogDO uEditorBlogDO) {
        return super.update(uEditorBlogDO);
    }



    @Caching(
            put = {
                    @CachePut(value = {"ueditor-blog"},key = "#a0.id",condition = "#a0.id!=null"),
                    @CachePut(value = {"ueditor-blog"},key = "'ueditor'+#a0.title",condition = "#a0.title!=null"),
                    @CachePut(value = {"ueditor-blog"},key = "'ueditor'+#a0.tag",condition = "#a0.tag!=null"),
                    @CachePut(value = {"ueditor-blog"},key = "'ueditor'+#a0.time",condition = "#a0.time!=null")
            }
    )
    public UEditorBlogDO updateSelective(UEditorBlogDO uEditorBlogDO) {
        super.updateSelective(uEditorBlogDO);
        //cache注解是通过切面实现的 调用同一类中的方法不会用到缓存 直接访问数据库获取
        return queryById(uEditorBlogDO.getId());
    }

}
