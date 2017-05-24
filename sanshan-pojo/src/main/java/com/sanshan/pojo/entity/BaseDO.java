package com.sanshan.pojo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 采用lombok缩减代码
 */
@Data
@NoArgsConstructor
public abstract class BaseDO implements Serializable{

    private Date created;
    private Date updated;


}
