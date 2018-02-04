package com.sanshan.pojo.elastic;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor
@Data
@ToString
@Document(indexName = "blogs",type = "ueditor")
public class ElasticUeditorBlogDO implements Serializable{

    private static final long serialVersionUID = 3755116058171427555L;

    @Id
    private Long id;

    private String user;

    private String title;

    private String content;

    private Date time;

    private String tag;

}
