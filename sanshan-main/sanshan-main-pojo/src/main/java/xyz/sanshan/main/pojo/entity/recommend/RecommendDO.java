package xyz.sanshan.main.pojo.entity.recommend;


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

    private List<BlogRecommendDO> recommendBlogs;

    private List<UserRecommendDO> recommendUsers;

    @Indexed(expireAfterSeconds = 2592000)
    private Date created;

}
