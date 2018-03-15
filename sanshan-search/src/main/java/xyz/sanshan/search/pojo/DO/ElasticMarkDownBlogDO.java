package xyz.sanshan.search.pojo.DO;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

@NoArgsConstructor
@Data
@ToString
@Document(indexName = "blogs",type = "markdown")
public class ElasticMarkDownBlogDO extends ElasticBaseEditorDO implements Serializable{

    private static final long serialVersionUID = -1154773418908534046L;

    @Id
    private Long id;

}
