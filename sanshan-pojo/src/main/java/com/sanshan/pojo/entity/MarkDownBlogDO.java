package com.sanshan.pojo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 采用了lombok进行缩减代码
 */
@Table(name = "markdown_blog")
@NoArgsConstructor()
@Data
public class MarkDownBlogDO extends EditorDO {

    @Id
    private long id;

}
