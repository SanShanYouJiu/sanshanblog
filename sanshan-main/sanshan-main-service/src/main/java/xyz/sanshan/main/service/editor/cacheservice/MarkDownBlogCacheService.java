package xyz.sanshan.main.service.editor.cacheservice;

import com.github.pagehelper.PageInfo;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import xyz.sanshan.main.pojo.entity.MarkDownBlogDO;
import xyz.sanshan.main.service.BaseServiceImpl;

import java.util.List;

/**
 *  DO存到缓存中
 */
@Service
public class MarkDownBlogCacheService extends BaseServiceImpl<MarkDownBlogDO> {

    @Override
    public List<MarkDownBlogDO> queryAll() {
        return super.queryAll();
    }



    @Cacheable(value = {"markdown-blog"})
    @Override
    public List<MarkDownBlogDO> queryListByWhere(MarkDownBlogDO example) {
        return super.queryListByWhere(example);
    }



    @Cacheable(value = {"markdown-blog"},key = "'markdown-blog:'+#a0")
    @Override
    public MarkDownBlogDO queryById(Long id) {
        return super.queryById(id);
    }



    @Override
    public PageInfo<MarkDownBlogDO> queryPageListByWhere(MarkDownBlogDO example, Integer page, Integer rows) {
        return super.queryPageListByWhere(example, page, rows);
    }


    @Override
    public Integer save(MarkDownBlogDO markDownBlog) {
        return super.save(markDownBlog);
    }


    @CacheEvict(value = {"markdown-blog"}, key = "'markdown-blog:'+#a0")
    @Override
    public Integer deleteById(Long id) {
        return super.deleteById(id);
    }


    @CachePut(value = {"markdown-blog"},key = "'markdown-blog:'+#a0.id")
    @Override
    public MarkDownBlogDO update(MarkDownBlogDO markDownBlogDO) {
        return super.update(markDownBlogDO);
    }


    @CachePut(value = {"markdown-blog"},key = "'markdown-blog:'+#a0.id")
    @Override
    public MarkDownBlogDO updateSelective(MarkDownBlogDO markDownBlogDO) {
        super.updateSelective(markDownBlogDO);
        //cache注解是通过切面实现的 调用同一类中的方法不会用到缓存 直接访问数据库获取
        return   queryById(markDownBlogDO.getId());
    }
}
