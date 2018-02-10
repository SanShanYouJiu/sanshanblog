package com.sanshan.service.editor;

import com.sanshan.pojo.dto.MarkDownBlogDTO;
import com.sanshan.pojo.dto.UeditorBlogDTO;
import com.sanshan.pojo.entity.BaseEditorDO;
import com.sanshan.pojo.entity.MarkDownBlogDO;
import com.sanshan.pojo.entity.UeditorBlogDO;
import com.sanshan.service.convent.MarkDownEditorConvert;
import com.sanshan.service.convent.UeditorEditorConvert;
import com.sanshan.service.search.ElasticSearchService;
import com.sanshan.service.user.cache.UserBlogCacheService;
import com.sanshan.pojo.dto.BaseEditorDTO;
import com.sanshan.service.vote.VoteService;
import com.sanshan.util.BlogIdGenerate;
import com.sanshan.util.exception.PropertyAccessException;
import com.sanshan.util.info.EditorTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class BlogOperation {

    private static final AtomicInteger POOL_NUMBER = new AtomicInteger(1);
    private ExecutorService pool = new ThreadPoolExecutor(1, 10, 3, TimeUnit.MINUTES, new SynchronousQueue<>(), (r) -> {
        Thread t = new Thread(r);
        t.setName("blog-operation-thread:" + POOL_NUMBER);
        return t;
    });

    @Autowired
    private ElasticSearchService elasticSearchService;

    @Autowired
    private UserBlogCacheService userBlogCacheService;

    @Autowired
    private UeditorFileService ueditorFileService;

    @Autowired
    private BlogIdGenerate blogIdGenerate;

    @Autowired
    private VoteService voteService;


    /**
     * markdown类型博客插入检查
     *
     * @param markDownBlogDO
     */
    public MarkDownBlogDO markdownBlogAddCheck(MarkDownBlogDO markDownBlogDO) {
        baseCheck(markDownBlogDO, markDownBlogDO.getId());
        return markDownBlogDO;
    }

    /**
     * markdown类型博客更新检测
     * @param markDownBlogDO
     * @return
     */
     public  MarkDownBlogDO markDownBlogUpdateCheck(MarkDownBlogDO markDownBlogDO){
         baseUpdateCheck(markDownBlogDO);
         return  markDownBlogDO;
     }

    /**
     * ueditor类型博客插入检查
     *
     * @param ueditorBlogDO
     * @return
     */
    public UeditorBlogDO ueditorBlogAddCheck(UeditorBlogDO ueditorBlogDO) {
        baseCheck(ueditorBlogDO, ueditorBlogDO.getId());
        return ueditorBlogDO;
    }

    /*
    ueditor 类型博客更新检测
     */
    public UeditorBlogDO ueditorBlogUpdateCheck(UeditorBlogDO ueditorBlogDO) {
        baseUpdateCheck(ueditorBlogDO);
        return ueditorBlogDO;
    }

    /**
     * Ueditor博客存入以及相关操作
     *
     * @param ueditorBlogDO
     * @param username
     */
    public void ueditorBlogAdd(UeditorBlogDO ueditorBlogDO, String username) {
        pool.execute(() -> {
            Long id = ueditorBlogDO.getId();
            String tag = ueditorBlogDO.getTag();
            String title = ueditorBlogDO.getTitle();
            String content = ueditorBlogDO.getContent();
            Date date = ueditorBlogDO.getTime();
            //更新User对应的blog缓存
            userBlogCacheService.userBlogRefresh(username);
            //加入到索引中
            if (tag != null) {
                blogIdGenerate.putTag(tag, id);
            }
            if (title != null) {
                blogIdGenerate.putTitle(title, id);
            }
            blogIdGenerate.putDate(date, id);
            //检测ueditor中上传的文件
            ueditorFileService.checkUeditorContentFile(id, content);

            UeditorBlogDTO ueditorBlogDTO = UeditorEditorConvert.doToDto(ueditorBlogDO);

            //加入到Es中
            elasticSearchService.ueditorBlogAdd(ueditorBlogDTO);
        });
    }

    /**
     * @param markDownBlogDO
     * @param username
     */
    public void markdownBlogAdd(MarkDownBlogDO markDownBlogDO, String username) {
        pool.execute(() -> {
            Long id = markDownBlogDO.getId();
            String tag = markDownBlogDO.getTag();
            String title = markDownBlogDO.getTitle();
            Date date = markDownBlogDO.getTime();
            //更新User对应的blog缓存
            userBlogCacheService.userBlogRefresh(username);
            //加入到索引中
            if (tag != null) {
                blogIdGenerate.putTag(tag, id);
            }
            if (title != null) {
                blogIdGenerate.putTitle(title, id);
            }
            blogIdGenerate.putDate(date, id);

            MarkDownBlogDTO markDownBlogDTO = MarkDownEditorConvert.doToDto(markDownBlogDO);
            //加入到Es中
            elasticSearchService.markdownBlogAdd(markDownBlogDTO);
        });
    }

    /**
     * @param markDownBlogDTO
     */
    public void markdownOtherUpdate(MarkDownBlogDTO markDownBlogDTO) {
        pool.execute(() -> {
            Long id = markDownBlogDTO.getId();
            baseOtherUpdateCache(markDownBlogDTO,id);
            elasticSearchService.markdownBlogAdd(markDownBlogDTO);
        });
    }

    /**
     * TODO 更新也要对内容中包含的文件进行检查
     *
     * @param ueditorBlogDTO
     */
    public void ueditorOtherUpdate(UeditorBlogDTO ueditorBlogDTO) {
        pool.execute(() -> {
            Long id = ueditorBlogDTO.getId();
            baseOtherUpdateCache(ueditorBlogDTO,id);
            elasticSearchService.ueditorBlogAdd(ueditorBlogDTO);
        });
    }

    /**
     * @param id
     * @param username
     */
    public void markdownDelete(Long id, String username) {
        pool.execute(() -> {
            userBlogCacheService.userBlogRefresh(username);
            blogIdGenerate.remove(id);
            blogDelete(id, EditorTypeEnum.MARKDOWN_EDITOR);
        });
    }

    /**
     * @param id
     * @param username
     */
    public void ueditorDelete(Long id, String username) {
        pool.execute(() -> {
            userBlogCacheService.userBlogRefresh(username);
            blogIdGenerate.remove(id);
            //审核ueditor博客中对应的文件
            ueditorFileService.deleteContentContainsFile(id);
            blogDelete(id, EditorTypeEnum.UEDITOR_EDITOR);
        });
    }

    /**
     * 博客删除的相关操作
     *
     * @param id
     */
    private void blogDelete(Long id, EditorTypeEnum type) {
        log.info("正在删除博客id为:{}的相关数据", id);
        voteService.deleteBlogVote(id);
        elasticSearchService.deleteBlog(id, type);
        log.info("删除博客id为:{}的相关数据完成", id);
    }


    /**
     * 一些共通的检测
     *
     * @param editorDO
     */
    private void baseCheck(BaseEditorDO editorDO, Long id) {
        String title = editorDO.getTitle().trim();
        editorDO.setTitle(title);
        String tag = editorDO.getTag().trim();
        editorDO.setTag(tag);
        if (tag.equals("")) {
            editorDO.setTag(null);
        }
        if (title.equals("")) {
            blogIdGenerate.remove(id);
            throw new PropertyAccessException("在提交的博客中找不到title类型 博客id为:" + id);
        }
    }

    /**
     *默认所有更新的选项不能为空或者纯空格
     * @param editorDO
     */
    private  void baseUpdateCheck(BaseEditorDO editorDO){
        String title = null;
        String tag = null;
        String content = null;
        //消除空格
        if (editorDO.getTitle()!=null) {
            title = editorDO.getTitle().trim();
            editorDO.setTitle(title);
        }
        if (editorDO.getTag()!=null) {
            tag = editorDO.getTag().trim();
            editorDO.setTag(tag);
        }
        if (editorDO.getContent()!=null) {
            content = editorDO.getContent().trim();
            editorDO.setContent(content);
        }

        //进行检查
        if (tag==null||tag.equals("")) {
            editorDO.setTag(null);
        }
        if (title==null||title.equals("")) {
            editorDO.setTitle(null);
        }
       if (content==null||content.equals("")){
           editorDO.setContent(null);
       }
    }


    /**
     * @param editorDTO
     * @param id
     */
    private void baseOtherUpdateCache(BaseEditorDTO editorDTO, Long id) {
        //更新User对应的blog缓存
        userBlogCacheService.userBlogRefresh(editorDTO.getUser());
        //加入到索引中
        if (editorDTO.getTag() != null) {
            blogIdGenerate.putTag(editorDTO.getTag(), id);
        }
        if (editorDTO.getTitle() != null) {
            blogIdGenerate.putTitle(editorDTO.getTitle(), id);
        }

    }

}
