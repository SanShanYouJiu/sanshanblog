package xyz.sanshan.search.service;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.data.elasticsearch.core.query.SourceFilter;
import org.springframework.stereotype.Service;
import xyz.sanshan.common.vo.ResponseMsgVO;
import xyz.sanshan.search.dao.MarkDownBlogInfoRepository;
import xyz.sanshan.search.dao.UeditorBlogInfoRepository;
import xyz.sanshan.search.dao.UserInfoRepository;
import xyz.sanshan.search.pojo.DO.ElasticBaseEditorDO;
import xyz.sanshan.search.pojo.DO.ElasticMarkDownBlogDO;
import xyz.sanshan.search.pojo.DO.ElasticUeditorBlogDO;
import xyz.sanshan.search.pojo.VO.ElasticResponseVO;
import xyz.sanshan.search.pojo.VO.ElasticSearchResultDTO;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;


/**
 *
 */
@Service
@Slf4j
public class ElasticSearchService {

    private static final AtomicInteger POOL_NUMBER = new AtomicInteger(1);
    private ExecutorService pool = new ThreadPoolExecutor(1, 10, 3, TimeUnit.MINUTES, new SynchronousQueue<>(), (r) -> {
        Thread t = new Thread(r);
        t.setName("es-operation-thread:" + POOL_NUMBER);
        return t;
    });

    @Autowired
    private ElasticsearchTemplate template;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private MarkDownBlogInfoRepository markDownBlogInfoRepository;

    @Autowired
    private UeditorBlogInfoRepository ueditorBlogInfoRepository;

    /**
     * 全部查询
     * 前期使用  应该尽量使用按类型搜索
     * 做返回字段判 不将content这种数据量大的进行返回到response中
     *
     * @param key
     * @param responseMsgVO
     */
    public void queryAll(String key, Integer pageRows, Integer pageNum, ResponseMsgVO responseMsgVO) {
        //Es默认是从0开始分页 前端默认是从1开始分页 需要注意
        pageNum=pageNum-1;
        SourceFilter sourceFilter = new FetchSourceFilter(new String[]{}, new String[]{"content"});
        SearchQuery query = new NativeSearchQueryBuilder().withQuery(matchQuery("_all", key)).withIndices("_all").withPageable(new PageRequest(pageNum, pageRows)).withSourceFilter(sourceFilter).build();
        template.query(query, (searchResponse) -> {
            ElasticResponseVO responseVO = assembleElasticReuturnSourceResponse(searchResponse);
            return responseMsgVO.buildOKWithData(responseVO);
        });
    }

    /**
     * 用户相关信息查询
     *
     * @param key
     * @param responseMsgVO
     */
    public void userInfoSearch(String key, int pageRows, int pageNum, ResponseMsgVO responseMsgVO) {
        pageNum=pageNum-1;
        SearchQuery query = new NativeSearchQueryBuilder().withQuery(matchQuery("_all", key)).withIndices("user-info").withTypes("user").withPageable(new PageRequest(pageNum, pageRows)).withFields().build();
        template.query(query, (searchResponse) -> {
            ElasticResponseVO responseVO = assembleElasticReuturnSourceResponse(searchResponse);
            return responseMsgVO.buildOKWithData(responseVO);
        });
    }

    /**
     * 用户名查询
     *
     * @param key
     * @param responseMsgVO
     */
    public void userNameSearch(String key, ResponseMsgVO responseMsgVO) {
        responseMsgVO.buildOKWithData(userInfoRepository.findByUsername(key));
    }


    /**
     * 博客信息查询
     *
     * @param key
     * @param responseMsgVO
     */
    public void blogInfoSearch(String key, Integer pageRows, Integer pageNum, ResponseMsgVO responseMsgVO) {
        pageNum=pageNum-1;
        SourceFilter sourceFilter = new FetchSourceFilter(new String[]{}, new String[]{"content"});
        SearchQuery query = new NativeSearchQueryBuilder().withQuery(matchQuery("_all", key)).withIndices("blogs").withPageable(new PageRequest(pageNum, pageRows)).withSourceFilter(sourceFilter).build();
        template.query(query, (searchResponse) -> {
            ElasticResponseVO responseVO = assembleElasticReuturnSourceResponse(searchResponse);
            return responseMsgVO.buildOKWithData(responseVO);
        });
    }

    /**
     * 博客内容查询
     *
     * @param key
     * @param responseMsgVO
     */
    public void blogContentSearch(String key, ResponseMsgVO responseMsgVO) {
        List<ElasticMarkDownBlogDO> markdownResult = markDownBlogInfoRepository.findByContent(key);
        List<ElasticUeditorBlogDO> ueditorResult = ueditorBlogInfoRepository.findByContent(key);
        List<ElasticBaseEditorDO> allResult = new LinkedList<>();
        allResult.addAll(markdownResult);
        allResult.addAll(ueditorResult);
        responseMsgVO.buildOKWithData(allResult);
    }


    /**
     * 组装返回Source结果
     *
     * @param searchResponse
     * @return
     */
    private ElasticResponseVO assembleElasticReuturnSourceResponse(SearchResponse searchResponse) {
        ElasticResponseVO responseVO = new ElasticResponseVO();
        List<ElasticSearchResultDTO> sources = new LinkedList<>();
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.hits();
        for (int i = 0; i < searchHits.length; i++) {
            ElasticSearchResultDTO hit = new ElasticSearchResultDTO();
            hit.setSource(searchHits[i].getSource());
            hit.setId(searchHits[i].getId());
            hit.setType(searchHits[i].getType());
            hit.setScore(searchHits[i].getScore());
            sources.add(hit);
        }
        responseVO.setTotal(hits.getTotalHits());
        responseVO.setResult(sources);
        return responseVO;
    }

    /**
     * 组装自定义filed结果
     *
     * @param searchResponse
     * @return
     */
    private ElasticResponseVO assembleElasticReuturnFiledsResponse(SearchResponse searchResponse) {
        ElasticResponseVO responseVO = new ElasticResponseVO();
        List<ElasticSearchResultDTO> fileds = new LinkedList<>();
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.hits();
        for (int i = 0; i < searchHits.length; i++) {
            ElasticSearchResultDTO hit = new ElasticSearchResultDTO();
            hit.setFields(searchHits[i].getFields());
            hit.setId(searchHits[i].getId());
            hit.setType(searchHits[i].getType());
            hit.setScore(searchHits[i].getScore());
            fileds.add(hit);
        }
        responseVO.setTotal(hits.getTotalHits());
        responseVO.setResult(fileds);
        return responseVO;
    }


}
