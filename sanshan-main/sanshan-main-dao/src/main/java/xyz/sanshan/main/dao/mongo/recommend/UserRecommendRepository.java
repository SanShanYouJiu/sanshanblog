package xyz.sanshan.main.dao.mongo.recommend;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import xyz.sanshan.main.pojo.entity.recommend.UserRecommendDO;

@Repository
public interface UserRecommendRepository extends MongoRepository<UserRecommendDO,String> {
}
