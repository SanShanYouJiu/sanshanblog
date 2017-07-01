package com.sanshan.pojo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public  abstract class EditorDO extends BaseDO {

    private String user;

    private String title;

    private String content;

    private Date time;

    private String tag;

}
