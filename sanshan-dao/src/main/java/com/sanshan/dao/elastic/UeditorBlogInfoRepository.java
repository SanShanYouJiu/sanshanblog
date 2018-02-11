package com.sanshan.dao.elastic;

import com.sanshan.pojo.elastic.ElasticUeditorBlogDO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface UeditorBlogInfoRepository extends ElasticsearchRepository<ElasticUeditorBlogDO,Long> {


    List<ElasticUeditorBlogDO> findByContent(String key);
}
