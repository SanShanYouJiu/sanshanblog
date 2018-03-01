package com.sanshan.dao.mongo;

import com.sanshan.pojo.entity.RecommendDO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecommendRepository extends MongoRepository<RecommendDO, String>,CustomRecommendRepository{

}
