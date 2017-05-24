package com.sanshan.pojo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@NoArgsConstructor
@ToString
public class MarkDownBlogDTO {

    private  Long id;

    private String user;

    private String title;

    private String content;

    private Date time;

    private String tag;

}
