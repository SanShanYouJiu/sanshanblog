package xyz.sanshan.web.controller.search;

import xyz.sanshan.service.search.ElasticSearchService;
import xyz.sanshan.service.vo.ResponseMsgVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 搜索相关
 */
@RestController
@RequestMapping("search")
public class SearchController {

    @Autowired
    private ElasticSearchService elasticSearchService;

    /**
     * 全部查询 会返回数据最多
     * @param key
     * @return
     */
    @GetMapping(value = "/all/{key}/pageRows:{pageRows}/pageNum:{pageNum}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO searchAll(@PathVariable(name = "key") String  key,@PathVariable(name = "pageRows")Integer pageRows,@PathVariable("pageNum")Integer pageNum){
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        elasticSearchService.queryAll(key,pageRows,pageNum,responseMsgVO);
        return responseMsgVO;
    }

    /**
     * 用户信息查询
     * @param key
     * @return
     */
    @GetMapping(value = "/user-info/{key}/pageRows:{pageRows}/pageNum:{pageNum}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO userInfoSearch(@PathVariable(name = "key")String key,@PathVariable(name = "pageRows")Integer pageRows,@PathVariable("pageNum")Integer pageNum){
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        elasticSearchService.userInfoSearch(key,pageRows,pageNum,responseMsgVO);
        return responseMsgVO;
    }

    /**
     * 用户名查询
     * @param key
     * @return
     */
    @GetMapping(value = "/username/{key}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO usernameSearch(@PathVariable(name = "key")String key){
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        elasticSearchService.userNameSearch(key,responseMsgVO);
        return responseMsgVO;
    }

    /**
     * 博客信息查询
     * @param key
     * @return
     */
    @GetMapping(value = "/blog-info/{key}/pageRows:{pageRows}/pageNum:{pageNum}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public  ResponseMsgVO blogInfoSearch(@PathVariable(name = "key")String key,@PathVariable(name = "pageRows") Integer pageRows,@PathVariable(name = "pageNum") Integer pageNum){
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        elasticSearchService.blogInfoSearch(key,pageRows,pageNum,responseMsgVO);
        return responseMsgVO;
    }

    /**
     * 博客内容查询
     */
    @GetMapping(value = "/blog-content/{key}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO blogContentSearch(@PathVariable(name = "key")String key){
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        elasticSearchService.blogContentSearch(key,responseMsgVO);
        return  responseMsgVO;
    }



}