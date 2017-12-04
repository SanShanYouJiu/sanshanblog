package com.sanshan.pojo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 */
@Table(name = "blog_vote")
@NoArgsConstructor
@Data
@ToString
public class BlogVoteDO extends BaseDO{

    @Id
    private Long id;

    @Column(name = "blog_id")
    private Long blogId;

    private Integer favours;

    private Integer treads;

    private String extra;

    public BlogVoteDO(Date created,Date updated, Long blogId, Integer favours, Integer treads) {
        super.setCreated(created);
        super.setUpdated(updated);
        this.blogId = blogId;
        this.favours = favours;
        this.treads = treads;
    }

}
