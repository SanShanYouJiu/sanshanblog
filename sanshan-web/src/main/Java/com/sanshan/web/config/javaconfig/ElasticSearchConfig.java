package com.sanshan.web.config.javaconfig;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 *
 */
@Configurable
@EnableElasticsearchRepositories(basePackages = "com.sanshan.dao.elastic")
public class ElasticSearchConfig {


}
