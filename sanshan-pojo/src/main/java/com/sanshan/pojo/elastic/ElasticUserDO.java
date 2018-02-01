package com.sanshan.pojo.elastic;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;


@NoArgsConstructor
@Data
@ToString
@Document(indexName = "user-info",type = "user")
public class ElasticUserDO {

    @Id
    private String id;

    /**
     *头像
     */
    private String avatar;

    /**
     * 昵称
     */
    private String username;

    /**
     * 电子邮箱
     */
    private String email;


    /**
     * 博客链接
     */
    private String blogLink;

    /**
     * 所得荣誉
     */
    private String honor;
}
