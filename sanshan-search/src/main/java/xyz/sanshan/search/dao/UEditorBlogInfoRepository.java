package xyz.sanshan.search.dao;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import xyz.sanshan.search.pojo.DO.ElasticUEditorBlogDO;

import java.util.List;

public interface UEditorBlogInfoRepository extends ElasticsearchRepository<ElasticUEditorBlogDO,Long> {


    List<ElasticUEditorBlogDO> findByContent(String key);
}
