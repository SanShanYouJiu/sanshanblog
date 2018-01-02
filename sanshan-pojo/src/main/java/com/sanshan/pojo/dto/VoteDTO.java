package com.sanshan.pojo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 */
@NoArgsConstructor
@Data
public class VoteDTO implements Serializable {

    private static final long serialVersionUID = -7095218826096566909L;

    private String ip;

    private Long blogId;

    private Boolean vote;


   public   VoteDTO addVote(String ip, long blogId, boolean vote){
       this.ip = ip;
       this.blogId = blogId;
       this.vote = vote;
       return  this;
   }

    public  VoteDTO decrVote(String ip, long blogId, boolean vote){
        this.ip = ip;
        this.blogId = blogId;
        this.vote = vote;
        return  this;
    }

    @Override
    public String toString() {
        return "VoteDTO{" +
                "ip='" + ip + '\'' +
                ", blogId=" + blogId +
                ", vote=" + vote +
                '}';
    }
}
