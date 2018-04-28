package xyz.sanshan.main.dao.mongo.recommend;

import xyz.sanshan.main.pojo.entity.recommend.RecommendDO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecommendRepository extends MongoRepository<RecommendDO, String>,CustomRecommendRepository {

}
