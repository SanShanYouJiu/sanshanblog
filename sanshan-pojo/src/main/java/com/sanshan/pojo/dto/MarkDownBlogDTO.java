package com.sanshan.pojo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author sanshan
 */
@Data
@NoArgsConstructor
@ToString
public class MarkDownBlogDTO extends BaseBlogEditorDTO implements Serializable{

    private static final long serialVersionUID = 5661725470938567952L;
    private  Long id;


}
