package xyz.sanshan.main.pojo.elastic;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;


@NoArgsConstructor
@Data
@ToString
@Document(indexName = "user-info", type = "user")
public class ElasticUserDO implements Serializable {

    private static final long serialVersionUID = 693337155482970397L;

    @Id
    private String id;

    /**
     *头像
     */
    @Field(type = FieldType.String,index = FieldIndex.not_analyzed)
    private String avatar;

    /**
     * 昵称
     */
    private String username;

    /**
     * 电子邮箱
      */
    @Field(type = FieldType.String,index = FieldIndex.not_analyzed)
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
