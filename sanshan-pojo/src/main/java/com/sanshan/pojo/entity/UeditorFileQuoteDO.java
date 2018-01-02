package com.sanshan.pojo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 */
@Table(name = "ueditor_file_quote")
@NoArgsConstructor
@Data
@ToString
public class UeditorFileQuoteDO extends BaseDO implements Serializable{

    private static final long serialVersionUID = 8437935278874560233L;

    @Id
    private Long id;

    private String filename;

    private Integer quote;

    private String extra;

    public UeditorFileQuoteDO(String filename, Integer quote, Date created,Date updated){
        this.filename=filename;
        this.quote=quote;
        this.setCreated(created);
        this.setUpdated(updated);
    }

}
