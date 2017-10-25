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
    private long id;

    @Column(name = "blog_id")
    private long blogId;

    private int favours;

    private int treads;

    private String extra;

    public BlogVoteDO(Date created,Date updated, long blogId, int favours, int treads) {
        super.setCreated(created);
        super.setUpdated(updated);
        this.blogId = blogId;
        this.favours = favours;
        this.treads = treads;
    }
}
