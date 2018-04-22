package xyz.sanshan.main.dao.mongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import xyz.sanshan.main.pojo.entity.recommend.RecommendDO;

public class RecommendRepositoryImpl  implements CustomRecommendRepository{
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * @return
     */
    @Override
    public RecommendDO findByNewestCreate() {
        Query query = new Query();
        query.with(new Sort(Sort.Direction.DESC,"created")).limit(1);
        RecommendDO recommendDO = mongoTemplate.findOne(query, RecommendDO.class);
        return recommendDO;
    }

}
