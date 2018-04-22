package xyz.sanshan.main.pojo.dto.recommend;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import xyz.sanshan.main.pojo.dto.CommonBlogDTO;
import xyz.sanshan.main.pojo.dto.UserDTO;

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

    private List<CommonBlogDTO> recommendBlogs;

    private List<UserDTO> recommendUsers;


}
