package xyz.sanshan.main.dao.mongo.recommend;


import xyz.sanshan.main.pojo.entity.recommend.RecommendDO;

public interface CustomRecommendRepository {

    RecommendDO findByNewestCreate();
}
