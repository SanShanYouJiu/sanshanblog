package xyz.sanshan.main.pojo.entity.recommend;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.index.Indexed;

import javax.persistence.Id;
import java.util.Date;

@Data
@ToString
@NoArgsConstructor
public class BlogRecommendDO {
    @Id
    private Long id;

    private String title;

    private String user;

    private Date time;

    private String tag;

    private Double recommendRate;

    @Indexed(expireAfterSeconds = 2592000)
    private Date created;
}
