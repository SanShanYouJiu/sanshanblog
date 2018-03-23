package xyz.sanshan.main.pojo.entity;


import xyz.sanshan.main.pojo.dto.BaseBlogDTO;
import xyz.sanshan.main.pojo.dto.UserDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@ToString
@Document()
@NoArgsConstructor
public class RecommendDO  implements Serializable {

    private String _id;

    private List<BaseBlogDTO> recommendBlogs;

    private List<UserDTO> recommendUsers;

    @Indexed(expireAfterSeconds = 2592000)
    private Date created;

}
