package com.sanshan.service.vo;

import com.sanshan.pojo.dto.MarkDownBlogDTO;
import com.sanshan.pojo.dto.UEditorBlogDTO;
import com.sanshan.util.info.EditorTypeEnum;
import lombok.Data;

import java.util.Date;

@Data
public class BlogVO {
    /**
     * true为markdown-blog
     * false则为UEditor-blog
     */
    private int type;

    private Long id;

    private String user;

    private String title;

    private String content;

    private Date time;

    private String tag;


    public BlogVO(UEditorBlogDTO uEditorBlog) {
        this.type= EditorTypeEnum.UEDITOR_EDITOR.getIndex();
        this.id = uEditorBlog.getId();
        this.user  =uEditorBlog.getUser();
        this.title = uEditorBlog.getTitle();
        this.content = uEditorBlog.getContent();
        this.time = uEditorBlog.getTime();
        this.tag = uEditorBlog.getTag();
    }

    public BlogVO(MarkDownBlogDTO markDownBlog) {
        this.type= EditorTypeEnum.MarkDown_EDITOR.getIndex();
        this.id = markDownBlog.getId();
        this.user  =markDownBlog.getUser();
        this.title = markDownBlog.getTitle();
        this.content = markDownBlog.getContent();
        this.time = markDownBlog.getTime();
        this.tag = markDownBlog.getTag();
    }



}
