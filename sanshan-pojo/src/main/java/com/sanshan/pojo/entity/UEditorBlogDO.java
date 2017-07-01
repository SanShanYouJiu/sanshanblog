package com.sanshan.pojo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "ueditor_blog")
@Data
@NoArgsConstructor
public class UEditorBlogDO extends EditorDO {

    private static final long serialVersionUID = 4108703926461957019L;

    @Id
    private long id;

}
