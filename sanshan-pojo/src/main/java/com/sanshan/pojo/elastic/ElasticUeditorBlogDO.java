package com.sanshan.pojo.elastic;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;

@NoArgsConstructor
@Data
@ToString
@Document(indexName = "blogs",type = "ueditor")
public class ElasticUeditorBlogDO {

    @Id
    private Long id;

    private String user;

    private String title;

    private String content;

    private Date time;

    private String tag;

}
