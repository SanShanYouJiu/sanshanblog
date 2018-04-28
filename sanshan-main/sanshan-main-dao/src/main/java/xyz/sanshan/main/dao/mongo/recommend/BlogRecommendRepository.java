package xyz.sanshan.main.dao.mongo.recommend;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import xyz.sanshan.main.pojo.entity.recommend.BlogRecommendDO;

@Repository
public interface BlogRecommendRepository extends MongoRepository<BlogRecommendDO, Long> {
}
