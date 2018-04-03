package xyz.sanshan.main.service.editor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import xyz.sanshan.common.info.EditorTypeEnum;
import xyz.sanshan.main.pojo.dto.UeditorBlogDTO;
import xyz.sanshan.main.pojo.entity.UeditorBlogDO;
import xyz.sanshan.main.service.convent.UeditorEditorConvert;
import xyz.sanshan.main.service.editor.cacheservice.UeditorBlogCacheService;
import xyz.sanshan.main.service.vo.JwtUser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Slf4j
public class UeditorBlogService {

    @Autowired
    private UeditorBlogCacheService cacheService;

    @Autowired
    private   BlogIdGenerate blogIdGenerate;

    @Autowired
    private BlogResourcesOperation blogOperation;


    /**
     * DTO查询
     * @param id 查询ID
     * @return
     */
    public UeditorBlogDTO queryDtoById(Long id){
        return UeditorEditorConvert.doToDto(cacheService.queryById(id));
    }

    /**
     * 存入ueditor博客
     * @param content
     * @param title
     * @param tag
     * @return
     */
    public Integer saveDO(String content, String title, String tag) {
        UeditorBlogDO uEditorBlogDO = new UeditorBlogDO();
        //使用IdMap生成的Id
        Long id = blogIdGenerate.getId(EditorTypeEnum.UEDITOR_EDITOR);
        uEditorBlogDO.setId(id);
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

          //检查
        UeditorBlogDO checkResult = blogOperation.ueditorBlogAddCheck(uEditorBlogDO);

        int result = cacheService.save(checkResult);
        //插入失败
        if (result == 0) {
            blogIdGenerate.removeIdMap(id);
            return 0;
        }

        blogOperation.ueditorBlogAdd(checkResult, user.getUsername());

        log.info("用户:{} 新增Ueditor博客Id为:{}",user.getUsername(),id);
        return  result;
    }

    /**
     * 参数不为空的进行更新操作
     * @param id
     * @param content
     * @param title
     * @param tag
     * @return
     */
    public Boolean  updateSelectiveDO(Long id,String content,String title,String tag){
        UeditorBlogDO uEditorBlogDO = new UeditorBlogDO();
        uEditorBlogDO.setId(id);
        uEditorBlogDO.setContent(content);
        uEditorBlogDO.setUpdated(new Date());
        uEditorBlogDO.setTag(tag);
        uEditorBlogDO.setTitle(title);
        //检查
        UeditorBlogDO checkResult= blogOperation.ueditorBlogUpdateCheck(uEditorBlogDO);

        UeditorBlogDTO uEditorBlogDTO = UeditorEditorConvert.doToDto(checkResult);
        cacheService.updateSelective(checkResult);
        blogOperation.ueditorOtherUpdate(uEditorBlogDTO);
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
        //更新User对应的blog缓存
        blogOperation.ueditorDelete(id,user.getUsername());
        log.info("用户:{}删除了Ueditor博客 Id为{}", user.getUsername(), id);
        return rows;
    }
}
