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
public class MarkDownBlogDO extends BaseEditorDO {

    private static final long serialVersionUID = 2532923739523906290L;

    @Id
    private Long id;

}
