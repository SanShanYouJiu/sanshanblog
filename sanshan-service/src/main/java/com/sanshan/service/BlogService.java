package com.sanshan.service;

import com.sanshan.service.editor.MarkDownBlogService;
import com.sanshan.service.editor.UeditorBlogService;
import com.sanshan.service.vo.BlogVO;
import com.sanshan.service.vo.ResponseMsgVO;
import com.sanshan.util.BlogIdGenerate;
import com.sanshan.util.info.EditorTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Service在新增Editor时需要修改
 */
@Service
public class BlogService {
    @Autowired
    MarkDownBlogService markDownBlogService;

    @Autowired
    UeditorBlogService uEditorBlogService;

    @Autowired
    BlogIdGenerate blogIdGenerate;

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


    public ResponseMsgVO removeBlog(Long id)  {
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
                throw  new NullPointerException("无法删除 ID已失效");
        }
        return new ResponseMsgVO().buildOK();
    }

    public List<BlogVO> queryAll() {
        List<BlogVO> blogs = new ArrayList<BlogVO>();
        int size = blogIdGenerate.getId();
        for (int i = 1; i <= size; i++) {
            if (Objects.isNull(getBlog((long) i))){
             continue;
            }else {
                blogs.add(getBlog((long) i));
            }
            }
        return blogs;
    }


}


