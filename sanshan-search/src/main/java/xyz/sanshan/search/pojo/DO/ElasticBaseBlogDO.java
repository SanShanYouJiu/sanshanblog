package xyz.sanshan.search.pojo.DO;

import lombok.Data;

import java.util.Date;

@Data
public  abstract class ElasticBaseBlogDO {

    private String user;

    private String title;

    private String content;

    private Date time;

    private String tag;
}
