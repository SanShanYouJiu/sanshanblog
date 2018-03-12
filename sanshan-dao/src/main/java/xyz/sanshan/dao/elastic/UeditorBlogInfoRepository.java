package xyz.sanshan.dao.elastic;

import xyz.sanshan.pojo.elastic.ElasticUeditorBlogDO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface UeditorBlogInfoRepository extends ElasticsearchRepository<ElasticUeditorBlogDO,Long> {


    List<ElasticUeditorBlogDO> findByContent(String key);
}
