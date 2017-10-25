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
@Table(name = " ip_blog_vote")
@NoArgsConstructor
@Data
@ToString
public class IpBlogVoteDO extends BaseDO{
    @Id
    private long id;

    private String ip;

    @Column(name = "blog_id")
    private long blogId;

    private boolean favour;

    private boolean tread;

    private String extra;

    public IpBlogVoteDO(Date created,Date updated,String ip, long blogId, boolean vote) {
        super.setCreated(created);
        super.setUpdated(updated);
        this.ip = ip;
        this.blogId = blogId;
        if (vote)
            favour = true;
        else
            tread = true;
    }
}
