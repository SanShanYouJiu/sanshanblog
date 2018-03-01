package com.sanshan.dao.mongo;


import com.sanshan.pojo.entity.RecommendDO;

public interface CustomRecommendRepository {

    RecommendDO findByNewestCreate();
}
