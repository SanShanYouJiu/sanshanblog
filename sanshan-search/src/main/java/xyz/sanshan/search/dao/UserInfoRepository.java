package xyz.sanshan.search.dao;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import xyz.sanshan.search.pojo.DO.ElasticUserDO;

import java.util.List;

public interface UserInfoRepository extends ElasticsearchRepository<ElasticUserDO,String> {
      List<ElasticUserDO> findByUsernameAndEmail(String username, String email);

      List<ElasticUserDO> findByUsername(String username);


}
