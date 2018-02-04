package com.sanshan.dao.elastic;

import com.sanshan.pojo.elastic.ElasticUeditorBlogDO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface UeditorBlogInfoRepository extends ElasticsearchRepository<ElasticUeditorBlogDO,Long> {


}
