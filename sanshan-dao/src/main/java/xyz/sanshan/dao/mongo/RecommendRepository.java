package xyz.sanshan.dao.mongo;

import xyz.sanshan.pojo.entity.RecommendDO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecommendRepository extends MongoRepository<RecommendDO, String>,CustomRecommendRepository{

}
