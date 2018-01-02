package com.sanshan.pojo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 */
@Table(name = " ip_blog_vote")
@NoArgsConstructor
@Data
@ToString
public class IpBlogVoteDO extends BaseDO implements Serializable{

    private static final long serialVersionUID = -1710798552070664045L;

    @Id
    private Long id;

    private String ip;

    @Column(name = "blog_id")
    private Long blogId;

    private Boolean favour = false;

    private Boolean tread = false;

    private String extra;

    public IpBlogVoteDO(Date created,Date updated,String ip, Long blogId, Boolean vote) {
        super.setCreated(created);
        super.setUpdated(updated);
        this.ip = ip;
        this.blogId = blogId;
        if (vote){
            favour = true;
            tread=false;
        }
        else{
            tread = true;
            favour=false;
        }
    }
}
