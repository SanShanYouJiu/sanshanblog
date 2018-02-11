package com.sanshan.dao.elastic;

import com.sanshan.pojo.elastic.ElasticMarkDownBlogDO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface MarkDownBlogInfoRepository extends ElasticsearchRepository<ElasticMarkDownBlogDO, Long> {

    List<ElasticMarkDownBlogDO>  findByContent(String key);

}
