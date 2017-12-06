package com.sanshan.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;

/**
 */
@NoArgsConstructor
@ToString
@Data
@AllArgsConstructor
public class IpBlogVoteDTO {

    @Id
    private Long id;

    private String ip;

    private Long blogId;

    private Boolean favour =false;

    private Boolean tread = false;

}
