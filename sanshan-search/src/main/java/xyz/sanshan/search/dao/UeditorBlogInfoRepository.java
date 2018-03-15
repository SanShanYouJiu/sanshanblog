package xyz.sanshan.search.dao;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import xyz.sanshan.search.pojo.DO.ElasticUeditorBlogDO;

import java.util.List;

public interface UeditorBlogInfoRepository extends ElasticsearchRepository<ElasticUeditorBlogDO,Long> {


    List<ElasticUeditorBlogDO> findByContent(String key);
}
