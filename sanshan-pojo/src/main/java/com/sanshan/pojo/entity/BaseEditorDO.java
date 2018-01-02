package com.sanshan.pojo.entity;

import lombok.Data;

import java.util.Date;

@Data
public  abstract class BaseEditorDO extends BaseDO {

    private String user;

    private String title;

    private String content;

    private Date time;

    private String tag;

}
