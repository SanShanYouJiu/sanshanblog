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
    public MarkDownBlogDO markdownBlogCheck(MarkDownBlogDO markDownBlogDO) {
        baseCheck(markDownBlogDO, markDownBlogDO.getId());
        return markDownBlogDO;
    }

    /**
     * ueditor类型博客插入检查
     *
     * @param ueditorBlogDO
     * @return
     */
    public UeditorBlogDO ueditorBlogCheck(UeditorBlogDO ueditorBlogDO) {
        baseCheck(ueditorBlogDO, ueditorBlogDO.getId());
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
    public void markdownUpdate(MarkDownBlogDTO markDownBlogDTO) {
        pool.execute(() -> {
            Long id = markDownBlogDTO.getId();
            String tag = markDownBlogDTO.getTag();
            String title = markDownBlogDTO.getTitle();
            String username = markDownBlogDTO.getUser();
            baseUpdateCache(id, username, title, tag);
            elasticSearchService.markdownBlogAdd(markDownBlogDTO);
        });
    }

    /**
     * TODO 更新也要对内容中包含的文件进行检查
     *
     * @param ueditorBlogDTO
     */
    public void ueditorUpdate(UeditorBlogDTO ueditorBlogDTO) {
        pool.execute(() -> {
            Long id = ueditorBlogDTO.getId();
            String tag = ueditorBlogDTO.getTag();
            String title = ueditorBlogDTO.getTitle();
            String username = ueditorBlogDTO.getUser();
            baseUpdateCache(id, username, title, tag);
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
        String title = editorDO.getTitle();
        String tag = editorDO.getTag();
        if (tag.equals("")) {
            editorDO.setTag(null);
        }
        if (title.equals("")) {
            blogIdGenerate.remove(id);
            throw new PropertyAccessException("在提交的博客中找不到title类型 博客id为:" + id);
        }
    }


    /**
     * @param id
     * @param username
     * @param title
     * @param tag
     */
    private void baseUpdateCache(Long id, String username, String title, String tag) {
        //更新User对应的blog缓存
        userBlogCacheService.userBlogRefresh(username);
        //加入到索引中
        if (tag != null) {
            blogIdGenerate.putTag(tag, id);
        }
        if (title != null) {
            blogIdGenerate.putTitle(title, id);
        }

    }

}
