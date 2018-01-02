package com.sanshan.pojo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * @author sanshan
 */
@Data
@NoArgsConstructor
@ToString
public class MarkDownBlogDTO implements Serializable{

    private static final long serialVersionUID = -9201214690365180565L;
    private  Long id;

    private String user;

    private String title;

    private String content;

    private Date time;

    private String tag;

}
