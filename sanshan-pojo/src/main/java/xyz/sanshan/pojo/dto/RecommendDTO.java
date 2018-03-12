package xyz.sanshan.pojo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@ToString
public class RecommendDTO {

    private String _id;

    private List<BaseBlogEditorDTO> recommendBlogs;

    private List<UserDTO> recommendUsers;


}
