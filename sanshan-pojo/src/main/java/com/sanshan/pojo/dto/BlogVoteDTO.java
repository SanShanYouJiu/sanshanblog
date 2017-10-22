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
    private long blogId;

    private int favours;

    private int treads;
}
