package xyz.sanshan.main.pojo.entity.recommend;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
@NoArgsConstructor
public class BlogRecommendDO {
    private Long id;

    private String title;

    private String user;

    private Date time;

    private String tag;

    private Double recommendRate;

    private Date created;
}
