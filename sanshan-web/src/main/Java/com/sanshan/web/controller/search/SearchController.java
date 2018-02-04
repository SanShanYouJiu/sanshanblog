package com.sanshan.web.controller.search;

import com.sanshan.service.search.ElasticSearchService;
import com.sanshan.service.vo.ResponseMsgVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 搜索相关
 * TODO 准备集成ES 搜索博客或者用户信息
 */
@RestController
@RequestMapping("search")
public class SearchController {

    @Autowired
    private ElasticSearchService elasticSearchService;

    @GetMapping(value = "/all/{key}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO searchAll(@PathVariable(name = "key") String  key){
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        elasticSearchService.queryAll(key,responseMsgVO);
        return responseMsgVO;
    }

}
