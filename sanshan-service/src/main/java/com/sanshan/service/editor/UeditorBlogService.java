package com.sanshan.service.editor;

import com.github.pagehelper.PageInfo;
import com.sanshan.pojo.dto.UEditorBlogDTO;
import com.sanshan.pojo.entity.UEditorBlogDO;
import com.sanshan.service.convent.UeditorEditorConvert;
import com.sanshan.service.editor.CacheService.UEditorBlogCacheService;
import com.sanshan.service.vo.JwtUser;
import com.sanshan.util.BlogIdGenerate;
import com.sanshan.util.info.EditorTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UeditorBlogService {

    @Autowired
    private UEditorBlogCacheService cacheService;

    @Autowired
    BlogIdGenerate blogIdGenerate;
    /**
     * DTO查询
     *
     * @return
     */
    public List<UEditorBlogDTO> queryDtoAll() {
        return UeditorEditorConvert.doToDtoList(cacheService.queryAll());
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
        return UeditorEditorConvert.doToDto(cacheService.queryById(id));
    }


    public Integer saveDO(String content, String title, String tag) {
        UEditorBlogDO uEditorBlogDO = new UEditorBlogDO();
        //使用IdMap生成的Id
        uEditorBlogDO.setId(blogIdGenerate.getId());
        uEditorBlogDO.setContent(content);
        uEditorBlogDO.setTag(tag);
        uEditorBlogDO.setTitle(title);
        uEditorBlogDO.setCreated(new Date());
        uEditorBlogDO.setUpdated(new Date());
        Date date = new Date();
        uEditorBlogDO.setTime(date);
        //获得当前用户
        JwtUser user = (JwtUser) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        uEditorBlogDO.setUser(user.getUsername());

        //加入到索引中
        blogIdGenerate.putTag(tag,blogIdGenerate.getId());
        blogIdGenerate.putTitle(title,blogIdGenerate.getId());
        blogIdGenerate.putDate(date,blogIdGenerate.getId());

        //加入IdMap对应
        blogIdGenerate.addIdMap(blogIdGenerate.getId(), EditorTypeEnum.UEDITOR_EDITOR);

        return cacheService.save(uEditorBlogDO);
    }

    public Boolean updateDO(UEditorBlogDO uEditorBlogDO){
        UeditorEditorConvert.doToDto(cacheService.update(uEditorBlogDO));
        return true;
    }

    public Boolean  updateSelectiveDO(Long id,String content,String title,String tag){
        UEditorBlogDO uEditorBlogDO = new UEditorBlogDO();
        uEditorBlogDO.setId(id);
        uEditorBlogDO.setContent(content);
        uEditorBlogDO.setUpdated(new Date());
        uEditorBlogDO.setTag(tag);
        uEditorBlogDO.setTitle(title);
        //加入到索引中
        if (tag!=null)
            blogIdGenerate.putTag(tag,id);
        if (title!=null)
            blogIdGenerate.putTitle(title,id);
        UeditorEditorConvert.doToDto(cacheService.updateSelective(uEditorBlogDO));
        return true;
    }

    public Integer deleteDOById(Long id) {
        return cacheService.deleteById(id);
    }
}
