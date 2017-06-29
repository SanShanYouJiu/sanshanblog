package com.sanshan.service;

import com.sanshan.service.editor.MarkDownBlogService;
import com.sanshan.service.editor.UeditorBlogService;
import com.sanshan.service.vo.BlogVO;
import com.sanshan.service.vo.ResponseMsgVO;
import com.sanshan.util.BlogIdGenerate;
import com.sanshan.util.exception.ERROR;
import com.sanshan.util.info.EditorTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Service在新增Editor时需要修改
 */
@Service
@Slf4j
public class BlogService {
    @Autowired
    MarkDownBlogService markDownBlogService;

    @Autowired
    UeditorBlogService uEditorBlogService;

    @Autowired
    BlogIdGenerate blogIdGenerate;


    public Long getCurrentId() {
        Long id = blogIdGenerate.getSize();
        for (Long i = id; i > 0; i--) {
            EditorTypeEnum type = blogIdGenerate.getType(i);
            switch (type) {
                case UEDITOR_EDITOR:
                    return i;
                case MarkDown_EDITOR:
                    return i;
                case Void_Id:
                    continue;
            }
        }
        log.error("获取当前ID错误:{}", id);
        throw new NullPointerException("未知错误");
    }

    public BlogVO getBlog(Long id) {
        EditorTypeEnum type = blogIdGenerate.getType(id);
        BlogVO blog = null;
        switch (type) {
            case UEDITOR_EDITOR:
                blog = new BlogVO(uEditorBlogService.queryDtoById(id));
                break;
            case MarkDown_EDITOR:
                blog = new BlogVO(markDownBlogService.queryDtoById(id));
                break;
            case Void_Id:
                break;
        }
        return blog;
    }


    /**
     * 查询对应tag标签的博客
     * @param tag
     * @return
     */
    public List<BlogVO> getBlogByTag(String tag) {
        List<BlogVO> blogVOS = new ArrayList<>();
        Set<Long> longs = blogIdGenerate.getTagMap(tag);
        if (Objects.isNull(longs))
            return  null;
        Long[] a = {};
        Long[] ids = longs.toArray(a);
        for (long i = 0; i < ids.length; i++) {
            BlogVO blogVO = getBlog(ids[(int) i]);
            blogVOS.add(blogVO);
        }
        return blogVOS;
    }

    public List queryTagAll() {
        List list = new ArrayList();
        Map<String,Set<Long>> map = blogIdGenerate.getIdTagCopy();
        for (Map.Entry<String, Set<Long>> entry : map.entrySet()) {
            list.add(entry.getKey());
        }
        return list;
    }

    public List queryTtitleAll() {
        List list = new ArrayList();
        Map<String,Set<Long>> map = blogIdGenerate.getIdTitleCopy();
        for (Map.Entry<String, Set<Long>> entry : map.entrySet()) {
            list.add(entry.getKey());
        }
        return list;
    }

    /**
     * 查询对应title标签的博客
     * @param title
     * @return
     */
    public List<BlogVO> getBlogByTitle(String title) {
        List<BlogVO> blogVOS = new ArrayList<>();
        Set<Long> longs = blogIdGenerate.getTitleMap(title);
        if (Objects.isNull(longs))
            return null;
        Long[] a={};
        Long[] ids = longs.toArray(a);
        for (long i = 0; i <ids.length ; i++) {
            BlogVO blogVO = getBlog(ids[Math.toIntExact(i)]);
            blogVOS.add(blogVO);
        }
        return blogVOS;
    }


    public ResponseMsgVO removeBlog(Long id) {
        EditorTypeEnum type = blogIdGenerate.getType(id);
        blogIdGenerate.remove(id);
        BlogVO blog = null;
        switch (type) {
            case UEDITOR_EDITOR:
               int result= uEditorBlogService.deleteDOById(id);
                if (result==0)
                    return new ResponseMsgVO().buildError(new ERROR(500, "删除对应Id为：" + id + "的博客失败"));
                break;
            case MarkDown_EDITOR:
                int result2 = markDownBlogService.deleteDOById(id);
                if (result2 == 0) {
                    return new ResponseMsgVO().buildError(new ERROR(500, "删除对应Id为：" + id + "的博客失败"));
                }
                break;
            case Void_Id:
                throw new NullPointerException("无法删除 ID已失效");
        }
        return new ResponseMsgVO().buildOK();
    }


    public List<BlogVO> queryAll() {
        List<BlogVO> blogs = new ArrayList<BlogVO>();
        Long size = blogIdGenerate.getSize();
        for (long i = 1; i <= size; i++) {
            if (Objects.isNull(getBlog(i))) {
                continue;
            } else {
                blogs.add(getBlog(i));
            }
        }
        return blogs;
    }

}


