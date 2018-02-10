package com.sanshan.dao.elastic;

import com.sanshan.pojo.elastic.ElasticUserDO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface UserInfoRepository extends ElasticsearchRepository<ElasticUserDO,String> {
      List<ElasticUserDO> findByUsernameAndEmail(String username, String email);

      List<ElasticUserDO> findByUsername(String username);

}
