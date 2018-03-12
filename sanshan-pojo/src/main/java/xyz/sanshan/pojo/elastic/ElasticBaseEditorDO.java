package xyz.sanshan.pojo.elastic;

import lombok.Data;

import java.util.Date;

@Data
public  abstract class ElasticBaseEditorDO {

    private String user;

    private String title;

    private String content;

    private Date time;

    private String tag;
}
