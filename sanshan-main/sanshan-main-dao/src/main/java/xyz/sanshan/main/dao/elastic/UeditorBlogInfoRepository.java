package xyz.sanshan.main.dao.elastic;

import xyz.sanshan.main.pojo.elastic.ElasticUeditorBlogDO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface UeditorBlogInfoRepository extends ElasticsearchRepository<ElasticUeditorBlogDO,Long> {


    List<ElasticUeditorBlogDO> findByContent(String key);
}
