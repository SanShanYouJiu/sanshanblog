package com.sanshan.service.search;

import com.sanshan.dao.elastic.UserInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ElasticSearchService {

    @Autowired
    private ElasticsearchTemplate template;

    @Autowired
    private UserInfoRepository userInfoRepository;





}
