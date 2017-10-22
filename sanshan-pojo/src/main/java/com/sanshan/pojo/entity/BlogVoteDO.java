package com.sanshan.pojo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 */
@Table(name = "blog_vote")
@NoArgsConstructor
@Data
@ToString
public class BlogVoteDO extends BaseDO{

    @Id
    @Column(name = "blog_id")
    private long blogId;

    private int favours;

    private int treads;

    private String extra;
}
