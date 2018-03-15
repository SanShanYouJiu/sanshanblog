package xyz.sanshan.main.service.vo;

import lombok.Data;
import org.elasticsearch.search.SearchHitField;

import java.util.Map;

@Data
public class ElasticSearchResultDTO {

    private String id ;
    private Map<String,Object>  source ;
    private Map<String, SearchHitField> fields ;
    private Float score;
    private String type;

}
