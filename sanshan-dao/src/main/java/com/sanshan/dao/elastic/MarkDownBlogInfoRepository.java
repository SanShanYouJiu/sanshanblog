package com.sanshan.dao.elastic;

import com.sanshan.pojo.elastic.ElasticMarkDownBlogDO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface MarkDownBlogInfoRepository extends ElasticsearchRepository<ElasticMarkDownBlogDO, Long> {

}
