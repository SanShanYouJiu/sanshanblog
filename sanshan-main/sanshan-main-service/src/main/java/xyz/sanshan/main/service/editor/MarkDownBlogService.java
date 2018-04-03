package xyz.sanshan.main.service.editor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import xyz.sanshan.common.info.EditorTypeEnum;
import xyz.sanshan.main.pojo.dto.MarkDownBlogDTO;
import xyz.sanshan.main.pojo.entity.MarkDownBlogDO;
import xyz.sanshan.main.service.convent.MarkDownEditorConvert;
import xyz.sanshan.main.service.editor.cacheservice.MarkDownBlogCacheService;
import xyz.sanshan.main.service.vo.JwtUser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Slf4j
public class MarkDownBlogService {
    @Autowired
    private MarkDownBlogCacheService cacheService;

    @Autowired
    private  BlogIdGenerate blogIdGenerate;

    @Autowired
    private BlogResourcesOperation blogOperation;



    /** DTO查询
     *
     */
    public MarkDownBlogDTO queryDtoById(Long id){
        return MarkDownEditorConvert.doToDto(cacheService.queryById(id));
    }


    /**
     *存入markdown博客
     * @param content
     * @param title
     * @param tag
     * @return
     */
    public Integer saveDO(String content, String title,String tag) {
        MarkDownBlogDO markDownBlog = new MarkDownBlogDO();
        Long id = blogIdGenerate.getId(EditorTypeEnum.MARKDOWN_EDITOR);
        markDownBlog.setId(id);
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

        //检查
        MarkDownBlogDO checkResult = blogOperation.markdownBlogAddCheck(markDownBlog);

        int result = cacheService.save(checkResult);
        //插入失败
        if (result == 0) {
            blogIdGenerate.removeIdMap(id);
            return 0;
        }
        blogOperation.markdownBlogAdd(checkResult,user.getUsername());

        log.info("用户:{} 新增Markdown博客Id为:{}",user.getUsername(),id);
        return result;
    }



    public Boolean  updateSelectiveDO(Long id,String content,String title,String tag){
        MarkDownBlogDO markDownBlogDO = new MarkDownBlogDO();
        markDownBlogDO.setId(id);
        markDownBlogDO.setContent(content);
        markDownBlogDO.setUpdated(new Date());
        markDownBlogDO.setTag(tag);
        markDownBlogDO.setTitle(title);
        //检查
        MarkDownBlogDO checkResult = blogOperation.markDownBlogUpdateCheck(markDownBlogDO);

        cacheService.updateSelective(checkResult);
        MarkDownBlogDTO markDownBlogDTO= MarkDownEditorConvert.doToDto(checkResult);

        blogOperation.markdownOtherUpdate(markDownBlogDTO);
        return true;
    }

    public Integer deleteDOById(Long id) {
        //获得当前用户
        JwtUser user = (JwtUser) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        //检查
        blogOperation.baseDeleteCheck(id, user.getUsername());
        //id去除匹配
        Integer rows = cacheService.deleteById(id);
        if (rows==0){
            return 0;
        }
        blogOperation.markdownDelete(id, user.getUsername());
        log.info("用户:{}删除了Markdown博客 Id为{}", user.getUsername(), id);
        return rows;
    }

}
