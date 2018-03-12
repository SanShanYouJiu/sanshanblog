package xyz.sanshan.dao.mongo;


import xyz.sanshan.pojo.entity.RecommendDO;

public interface CustomRecommendRepository {

    RecommendDO findByNewestCreate();
}
