package xyz.sanshan.main.pojo.entity.recommend;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
@NoArgsConstructor
public class UserRecommendDO {

    private String _id;

    private String avatar;

    private String username;

    private String email;

    private Double recommendRate;

    private Date created;
}
