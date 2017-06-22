package com.sanshan.service;

import com.sanshan.pojo.dto.MarkDownBlogDTO;
import com.sanshan.pojo.dto.UEditorBlogDTO;
import com.sanshan.service.convent.BlogConvert;
import com.sanshan.service.editor.MarkDownBlogService;
import com.sanshan.service.editor.UeditorBlogService;
import com.sanshan.service.vo.BlogVO;
import com.sanshan.service.vo.ResponseMsgVO;
import com.sanshan.util.BlogIdGenerate;
import com.sanshan.util.info.EditorTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        List<UEditorBlogDTO> uEditorBlogDTOS = uEditorBlogService.queryByTag(tag);
        List<MarkDownBlogDTO> markDownBlogDTOS = markDownBlogService.queryByTag(tag);
        blogVOS.addAll(BlogConvert.MarkdownDoToDtoList(markDownBlogDTOS));
        blogVOS.addAll(BlogConvert.UeditorDoToDtoList(uEditorBlogDTOS));
        return blogVOS;
    }

    /**
     * 查询对应title标签的博客
     * @param title
     * @return
     */
    public List<BlogVO> getBlogByTitle(String title) {
        List<BlogVO> blogVOS = new ArrayList<>();
        List<UEditorBlogDTO> uEditorBlogDTOS = uEditorBlogService.queryByTitle(title);
        List<MarkDownBlogDTO> markDownBlogDTOS = markDownBlogService.queryByTitle(title);
        blogVOS.addAll(BlogConvert.MarkdownDoToDtoList(markDownBlogDTOS));
        blogVOS.addAll(BlogConvert.UeditorDoToDtoList(uEditorBlogDTOS));
        return blogVOS;
    }


    public ResponseMsgVO removeBlog(Long id) {
        EditorTypeEnum type = blogIdGenerate.getType(id);
        blogIdGenerate.remove(id);
        BlogVO blog = null;
        switch (type) {
            case UEDITOR_EDITOR:
                uEditorBlogService.deleteDOById(id);
                break;
            case MarkDown_EDITOR:
                markDownBlogService.deleteDOById(id);
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


