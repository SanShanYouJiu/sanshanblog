package xyz.sanshan.main.pojo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 */
@Data
@NoArgsConstructor
@ToString
public class RecommendDTO implements Serializable {

    private static final long serialVersionUID = 8274436895066167738L;

    private String _id;

    private List<BaseBlogDTO> recommendBlogs;

    private List<UserDTO> recommendUsers;


}
