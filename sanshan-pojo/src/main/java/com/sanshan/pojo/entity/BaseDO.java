package com.sanshan.pojo.entity;

import lombok.Data;

import java.util.Date;

/**
 * 采用lombok缩减代码
 */
@Data
public abstract class BaseDO {

    private Date created;
    private Date updated;


}
