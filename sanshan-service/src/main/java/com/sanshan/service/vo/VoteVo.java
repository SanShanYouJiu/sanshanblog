package com.sanshan.service.vo;

import lombok.Data;

import java.io.Serializable;

/**
 */
@Data
public class VoteVo implements Serializable {
    private static final long serialVersionUID = -7033015867655070467L;

    private String id;

    private long blogId;

    private boolean vote;

    public VoteVo() {
    }

    public VoteVo(String id, long blogId, boolean vote) {
        this.id = id;
        this.blogId = blogId;
        this.vote = vote;
    }

    @Override
    public String toString() {
        return "VoteVo{" +
                "id=" + id +
                ", blogId=" + blogId +
                ", vote=" + vote +
                '}';
    }
}
