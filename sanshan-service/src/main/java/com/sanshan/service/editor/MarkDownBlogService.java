package com.sanshan.service.editor;

import com.github.pagehelper.PageInfo;
import com.sanshan.pojo.dto.MarkDownBlogDTO;
import com.sanshan.pojo.entity.MarkDownBlogDO;
import com.sanshan.service.convent.MarkDownEditorConvert;
import com.sanshan.service.editor.CacheService.MarkDownBlogCacheService;
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
public class MarkDownBlogService {
    @Autowired
    private   MarkDownBlogCacheService cacheService;

    @Autowired
    private  BlogIdGenerate blogIdGenerate;

    @Autowired
    private UserBlogCacheService userBlogCacheService;
    /**
     * DTO查询
     *
     * @return
     */
    public List<MarkDownBlogDTO> queryDtoAll() {
        return MarkDownEditorConvert.doToDtoList(cacheService.queryAll());
    }


    /**
     * DTO查询
     * @param example 查询条件
     * @return
     */
    public List<MarkDownBlogDTO> queryDtoListByWhere(MarkDownBlogDO example) {
        return MarkDownEditorConvert.doToDtoList(cacheService.queryListByWhere(example));
    }


    /** DTO查询
     *
     */
    public MarkDownBlogDTO queryDtoById(Long id){
        return MarkDownEditorConvert.doToDto(cacheService.queryById(id));
    }

    /**
     * DTO查询
     *
     * @param example 条件
     * @param page    页数
     * @param rows    行数
     * @return
     */
    public PageInfo<MarkDownBlogDTO> queryDtoPageListByWhere(MarkDownBlogDO example, Integer page, Integer rows) {
        PageInfo<MarkDownBlogDO> markDownBlogDOPageInfo = cacheService.queryPageListByWhere(example, page, rows);
        return MarkDownEditorConvert.doToDtoPage(markDownBlogDOPageInfo);
    }

    public Integer saveDO(String content, String title,String tag) {
        MarkDownBlogDO markDownBlog = new MarkDownBlogDO();
        markDownBlog.setId(blogIdGenerate.getId());
        markDownBlog.setContent(content);
        markDownBlog.setTag(tag);
        markDownBlog.setTitle(title);
        markDownBlog.setCreated(new Date());
        markDownBlog.setUpdated(new Date());
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
        markDownBlog.setTime(date);
        //获得当前用户
        JwtUser user = (JwtUser) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        markDownBlog.setUser(user.getUsername());

        int result = cacheService.save(markDownBlog);
        //插入失败
        if (result == 0) {
            return 0;
        }
        //更新User对应的blog缓存
        userBlogCacheService.userBlogRefresh(user.getUsername());
        //加入到索引中
        if (tag!=null)
        blogIdGenerate.putTag(tag,blogIdGenerate.getId());
        if (title!=null)
        blogIdGenerate.putTitle(title,blogIdGenerate.getId());
        blogIdGenerate.putDate(date,blogIdGenerate.getId());

        //加入IdMap对应
        blogIdGenerate.addIdMap(blogIdGenerate.getId(), EditorTypeEnum.MarkDown_EDITOR);
        log.info("用户:{}新增Markdown博客Id为:{}",user.getUsername(),markDownBlog.getId());
        return result;
    }

    @Deprecated
    public MarkDownBlogDTO updateDO(MarkDownBlogDO markDownBlogDO){
        long id=markDownBlogDO.getId();
        String tag = markDownBlogDO.getTag();
        Date date=markDownBlogDO.getTime();
        String title = markDownBlogDO.getTitle();

       MarkDownBlogDTO markDownBlogDTO= MarkDownEditorConvert.doToDto(cacheService.update(markDownBlogDO));

        //更新User对应的blog缓存
        userBlogCacheService.userBlogRefresh(markDownBlogDTO.getUser());
        //加入到索引中
        if (tag!=null)
            blogIdGenerate.putTag(tag,id);
        if (title!=null)
            blogIdGenerate.putTitle(title,id);
        blogIdGenerate.putDate(date,id);
        return markDownBlogDTO;
    }


    public Boolean  updateSelectiveDO(Long id,String content,String title,String tag){
        MarkDownBlogDO markDownBlogDO = new MarkDownBlogDO();
        markDownBlogDO.setId(id);
        markDownBlogDO.setContent(content);
        markDownBlogDO.setUpdated(new Date());
        markDownBlogDO.setTag(tag);
        markDownBlogDO.setTitle(title);

        MarkDownBlogDTO markDownBlogDTO= MarkDownEditorConvert.doToDto(cacheService.updateSelective(markDownBlogDO));
        //更新User对应的blog缓存
        userBlogCacheService.userBlogRefresh(markDownBlogDTO.getUser());
        //加入到索引中
        if (tag!=null)
            blogIdGenerate.putTag(tag,id);
        if (title!=null)
            blogIdGenerate.putTitle(title,id);
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
        userBlogCacheService.userBlogRefresh(user.getUsername());
        blogIdGenerate.remove(id);
        log.info("用户:{}删除了Markdown博客 Id为{}", user.getUsername(), id);
        return rows;
    }

}
