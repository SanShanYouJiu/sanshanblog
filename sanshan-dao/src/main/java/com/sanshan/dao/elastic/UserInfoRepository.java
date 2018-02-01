package com.sanshan.dao.elastic;

import com.sanshan.pojo.elastic.ElasticUserDO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface UserInfoRepository extends ElasticsearchRepository<ElasticUserDO,String> {

}
