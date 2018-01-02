package com.sanshan.pojo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 */
@Table(name = "ueditor_id_file_map")
@NoArgsConstructor()
@Data
public class UeditorIdFileMapDO extends BaseDO implements Serializable{

    private static final long serialVersionUID = 2454555464720229972L;

    @Id
    private Long id;

    @Column(name = "blog_id")
    private Long blod_id;

    private String filename;

    private String extra;

    public UeditorIdFileMapDO(Long blod_id, String filename,Date created,Date updated) {
        this.blod_id = blod_id;
        this.filename = filename;
        this.setCreated(created);
        this.setUpdated(updated);
    }
}
