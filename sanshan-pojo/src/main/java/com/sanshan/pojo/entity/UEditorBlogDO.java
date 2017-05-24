package com.sanshan.pojo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "ueditor_blog")
@Data
@NoArgsConstructor
public class UEditorBlogDO extends EditorDO {

    @Id
    private long id;

}
