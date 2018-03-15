package xyz.sanshan.search.dao;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import xyz.sanshan.search.pojo.DO.ElasticMarkDownBlogDO;

import java.util.List;

public interface MarkDownBlogInfoRepository extends ElasticsearchRepository<ElasticMarkDownBlogDO, Long> {

    List<ElasticMarkDownBlogDO>  findByContent(String key);

}
