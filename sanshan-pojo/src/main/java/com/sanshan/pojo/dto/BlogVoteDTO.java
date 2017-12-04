package com.sanshan.pojo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 */
@NoArgsConstructor
@Data
@ToString
public class BlogVoteDTO {

    private Long id;

    private Long blogId;

    private Integer favours;

    private Integer treads;

    public BlogVoteDTO(Long blogId, Integer favours, Integer treads) {
        this.blogId = blogId;
        this.favours = favours;
        this.treads = treads;
    }
}
