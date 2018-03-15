package xyz.sanshan.search.pojo.DTO;

import lombok.Data;

import java.util.Date;

@Data
public  abstract class BaseBlogEditorDTO {

    private String user;

    private String title;

    private String content;

    private Date time;

    private String tag;
}
