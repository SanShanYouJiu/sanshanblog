package com.sanshan.dao.mongo;

import com.sanshan.pojo.entity.RecommendDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

public class RecommendRepositoryImpl  implements CustomRecommendRepository{
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * TODO 完成获得最新推荐数据
     * @return
     */
    @Override
    public RecommendDO findByNewestCreate() {
        Query query = new Query();
        //query.addCriteria(new Criteria("created").maxDistance())
        //mongoTemplate.findOne()
        return null;
    }

}
