package com.sanshan.pojo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;

/**
 */
@NoArgsConstructor
@ToString
@Data
public class IpBlogVoteDTO {

    @Id
    private long id;

    private String ip;

    private long blogId;

    private boolean favour;

    private boolean tread;

}
