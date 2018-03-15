package xyz.sanshan.main.dao.elastic;

import xyz.sanshan.main.pojo.elastic.ElasticMarkDownBlogDO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface MarkDownBlogInfoRepository extends ElasticsearchRepository<ElasticMarkDownBlogDO, Long> {

    List<ElasticMarkDownBlogDO>  findByContent(String key);

}
