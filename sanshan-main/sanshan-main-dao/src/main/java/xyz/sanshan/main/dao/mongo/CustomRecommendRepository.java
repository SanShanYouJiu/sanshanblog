package xyz.sanshan.main.dao.mongo;


import xyz.sanshan.main.pojo.entity.recommend.RecommendDO;

public interface CustomRecommendRepository {

    RecommendDO findByNewestCreate();
}
