package xyz.sanshan.main.dao.mongo;


import xyz.sanshan.main.pojo.entity.RecommendDO;

public interface CustomRecommendRepository {

    RecommendDO findByNewestCreate();
}
