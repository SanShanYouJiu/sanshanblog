package com.sanshan.service.editor;

import com.github.pagehelper.PageInfo;
import com.sanshan.pojo.dto.UEditorBlogDTO;
import com.sanshan.pojo.entity.UEditorBlogDO;
import com.sanshan.service.convent.UeditorEditorConvent;
import com.sanshan.service.editor.CacheService.UEditorBlogCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UeditorBlogService {

    @Autowired
   private UEditorBlogCacheService cacheService;

    /**
     * DTO查询
     *
     * @return
     */
    public List<UEditorBlogDTO> queryDtoAll() {
        return UeditorEditorConvent.doToDtoList(cacheService.queryAll());
    }

    /**
     * DTO查询
     * @param example 条件
     * @return
     */
    public List<UEditorBlogDTO> queryDtoListByWhere(UEditorBlogDO example) {
        return UeditorEditorConvent.doToDtoList(cacheService.queryListByWhere(example));
    }

    /**
     * DTO查询
     * @param example 条件
     * @param page   页数
     * @param rows    行数
     * @return
     */
    public PageInfo<UEditorBlogDTO> queryDtoPageListByWhere(UEditorBlogDO example, Integer page, Integer rows){
        PageInfo<UEditorBlogDO>  uEditorBlogDOPageInfo=cacheService.queryPageListByWhere(example, page, rows);
        return UeditorEditorConvent.doToDtoPage(uEditorBlogDOPageInfo);
    }


    /**
     * DTO查询
     * @param id 查询ID
     * @return
     */
    public  UEditorBlogDTO queryDtoById(Long id){
        return UeditorEditorConvent.doToDto(cacheService.queryById(id));
    }


    public Integer saveDO(UEditorBlogDO uEditorBlog) {
        return cacheService.save(uEditorBlog);
    }

    public Integer deleteDOById(Long id) {
        return cacheService.deleteById(id);
    }
}
