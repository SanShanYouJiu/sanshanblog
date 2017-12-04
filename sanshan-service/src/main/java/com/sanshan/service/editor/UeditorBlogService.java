package com.sanshan.service.editor;

import com.github.pagehelper.PageInfo;
import com.sanshan.pojo.dto.UeditorBlogDTO;
import com.sanshan.pojo.entity.UeditorBlogDO;
import com.sanshan.service.convent.UeditorEditorConvert;
import com.sanshan.service.editor.cacheservice.UeditorBlogCacheService;
import com.sanshan.service.user.cache.UserBlogCacheService;
import com.sanshan.service.vo.JwtUser;
import com.sanshan.util.BlogIdGenerate;
import com.sanshan.util.info.EditorTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class UeditorBlogService {

    @Autowired
    private UeditorBlogCacheService cacheService;

    @Autowired
    private   BlogIdGenerate blogIdGenerate;

    @Autowired
    private UserBlogCacheService userBlogCacheService;
    /**
     * DTO查询
     *
     * @return
     */
    public List<UeditorBlogDTO> queryDtoAll() {
        return UeditorEditorConvert.doToDtoList(cacheService.queryAll());
    }

    /**
     * DTO查询
     * @param example 条件
     * @return
     */
    public List<UeditorBlogDTO> queryDtoListByWhere(UeditorBlogDO example) {
        return UeditorEditorConvert.doToDtoList(cacheService.queryListByWhere(example));
    }

    /**
     * DTO查询
     * @param example 条件
     * @param page   页数
     * @param rows    行数
     * @return
     */
    public PageInfo<UeditorBlogDTO> queryDtoPageListByWhere(UeditorBlogDO example, Integer page, Integer rows){
        PageInfo<UeditorBlogDO>  uEditorBlogDOPageInfo=cacheService.queryPageListByWhere(example, page, rows);
        return UeditorEditorConvert.doToDtoPage(uEditorBlogDOPageInfo);
    }


    /**
     * DTO查询
     * @param id 查询ID
     * @return
     */
    public UeditorBlogDTO queryDtoById(Long id){
        return UeditorEditorConvert.doToDto(cacheService.queryById(id));
    }


    public Integer saveDO(String content, String title, String tag) {
        UeditorBlogDO uEditorBlogDO = new UeditorBlogDO();
        //使用IdMap生成的Id
        uEditorBlogDO.setId(blogIdGenerate.getId());
        uEditorBlogDO.setContent(content);
        uEditorBlogDO.setTag(tag);
        uEditorBlogDO.setTitle(title);
        uEditorBlogDO.setCreated(new Date());
        uEditorBlogDO.setUpdated(new Date());
        Date currentDate = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString=format.format(currentDate);
        Date date= null;
        try {
            date = format.parse(dateString);
        } catch (ParseException e) {
            log.error("解析{}失败",dateString);
            e.printStackTrace();
        }
        uEditorBlogDO.setTime(date);
        //获得当前用户
        JwtUser user = (JwtUser) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
          uEditorBlogDO.setUser(user.getUsername());
        int result = cacheService.save(uEditorBlogDO);
        //插入失败
        if (result == 0) {
            return 0;
        }

        //更新User对应的blog缓存
        userBlogCacheService.userBlogRefresh(user.getUsername());

        //加入到索引中
        if (tag!=null){
            blogIdGenerate.putTag(tag,blogIdGenerate.getId());
        }
        if (title!=null){
            blogIdGenerate.putTitle(title,blogIdGenerate.getId());
        }
        blogIdGenerate.putDate(date,blogIdGenerate.getId());

        //加入IdMap对应
        blogIdGenerate.addIdMap(blogIdGenerate.getId(), EditorTypeEnum.UEDITOR_EDITOR);
        log.info("用户:{}新增Ueditor博客Id为:{}",user.getUsername(),uEditorBlogDO.getId());
        return  result;
    }

    @Deprecated
    public Boolean updateDO(UeditorBlogDO uEditorBlogDO){
        long id=uEditorBlogDO.getId();
        String tag = uEditorBlogDO.getTag();
        Date date=uEditorBlogDO.getTime();
        String title = uEditorBlogDO.getTitle();

        UeditorEditorConvert.doToDto(cacheService.update(uEditorBlogDO));

        //更新User对应的blog缓存
        userBlogCacheService.userBlogRefresh(uEditorBlogDO.getUser());
        //加入到索引中
        if (tag!=null){
            blogIdGenerate.putTag(tag,id);
        }
        if (title!=null){
            blogIdGenerate.putTitle(title,id);
        }
        blogIdGenerate.putDate(date,id);
        return true;
    }

    public Boolean  updateSelectiveDO(Long id,String content,String title,String tag){
        UeditorBlogDO uEditorBlogDO = new UeditorBlogDO();
        uEditorBlogDO.setId(id);
        uEditorBlogDO.setContent(content);
        uEditorBlogDO.setUpdated(new Date());
        uEditorBlogDO.setTag(tag);
        uEditorBlogDO.setTitle(title);

       UeditorBlogDTO uEditorBlogDTO= UeditorEditorConvert.doToDto(cacheService.updateSelective(uEditorBlogDO));
        //更新User对应的blog缓存
        userBlogCacheService.userBlogRefresh(uEditorBlogDTO.getUser());
        //加入到索引中
        if (tag!=null){
            blogIdGenerate.putTag(tag,id);
        }
        if (title!=null){
            blogIdGenerate.putTitle(title,id);
        }
        return true;
    }

    public Integer deleteDOById(Long id) {
        //获得当前用户
        JwtUser user = (JwtUser) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        //id去除匹配
        Integer rows = cacheService.deleteById(id);
        if (rows==0){
            return 0;
        }
        //更新User对应的blog缓存
        userBlogCacheService.userBlogRefresh(user.getUsername());
        //TODO: 删除Ueditor中博客对应的文件
        blogIdGenerate.remove(id);
        log.info("用户:{}删除了Ueditor博客 Id为{}", user.getUsername(), id);
        return rows;
    }
}
