package com.sanshan.service.editor;

import com.github.pagehelper.PageInfo;
import com.sanshan.pojo.dto.UEditorBlogDTO;
import com.sanshan.pojo.entity.UEditorBlogDO;
import com.sanshan.service.convent.UeditorEditorConvert;
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
        return UeditorEditorConvert.doToDtoList(cacheService.queryAll());
    }


    /**
     * 通过DTO查询
     * @param tag
     * @return
     */
    public List<UEditorBlogDTO> queryByTag(String tag){
        UEditorBlogDO uEditorBlogDO = new UEditorBlogDO();
        uEditorBlogDO.setTag(tag);
        return UeditorEditorConvert.doToDtoList(cacheService.queryByTag(uEditorBlogDO));
    }


    public List<UEditorBlogDTO> queryByTitle(String title) {
        UEditorBlogDO uEditorBlogDO = new UEditorBlogDO();
        uEditorBlogDO.setTitle(title);
        return UeditorEditorConvert.doToDtoList(cacheService.queryByTitle(uEditorBlogDO));
    }

    /**
     * DTO查询
     * @param example 条件
     * @return
     */
    public List<UEditorBlogDTO> queryDtoListByWhere(UEditorBlogDO example) {
        return UeditorEditorConvert.doToDtoList(cacheService.queryListByWhere(example));
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
        return UeditorEditorConvert.doToDtoPage(uEditorBlogDOPageInfo);
    }


    /**
     * DTO查询
     * @param id 查询ID
     * @return
     */
    public  UEditorBlogDTO queryDtoById(Long id){
        UEditorBlogDO uEditorBlogDO = new UEditorBlogDO();
        uEditorBlogDO.setId(id);
        return UeditorEditorConvert.doToDto(cacheService.queryOne(uEditorBlogDO));
    }


    public Integer saveDO(UEditorBlogDO uEditorBlog) {
        return cacheService.save(uEditorBlog);
    }

    public Boolean updateDO(UEditorBlogDO uEditorBlogDO){
          UeditorEditorConvert.doToDto(cacheService.update(uEditorBlogDO));
        return true;
    }

    public Boolean  updateSelectiveDO(UEditorBlogDO uEditorBlogDO){
        UeditorEditorConvert.doToDto(cacheService.updateSelective(uEditorBlogDO));
        return true;
    }

    public Integer deleteDOById(Long id) {
        return cacheService.deleteById(id);
    }
}
