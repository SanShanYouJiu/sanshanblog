package com.sanshan.pojo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@ToString
public class UeditorBlogDTO  implements Serializable{

    private static final long serialVersionUID = 4347635814760464628L;
    private  Long id;

    private String user;

    private String title;

    private String content;

    private Date time;

    private String tag;
}
