package com.sanshan.pojo.dto;

import lombok.Data;

import java.io.Serializable;

/**
 */
@Data
public class VoteDTO implements Serializable {
    private static final long serialVersionUID = -7033015867655070467L;

    private String id;

    private Long blogId;

    private Boolean vote;

    public VoteDTO() {
    }

    public VoteDTO(String id, long blogId, boolean vote) {
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
