package xyz.sanshan.pojo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 */
@NoArgsConstructor
@Data
@ToString
public class BlogVoteDTO implements Serializable {

    private static final long serialVersionUID = 607261739358316277L;
    
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
