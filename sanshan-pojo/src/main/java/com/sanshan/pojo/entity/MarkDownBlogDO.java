package com.sanshan.pojo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 采用了lombok进行缩减代码
 */
@Table(name = "markdown_blog")
@NoArgsConstructor()
@Data
public class MarkDownBlogDO extends BaseEditorDO  implements Serializable{


    private static final long serialVersionUID = 4504221282215641915L;
    @Id
    private Long id;

    private String extra;

}
