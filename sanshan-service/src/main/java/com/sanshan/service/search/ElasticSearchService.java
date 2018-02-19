package com.sanshan.service.search;

import com.sanshan.dao.elastic.MarkDownBlogInfoRepository;
import com.sanshan.dao.elastic.UeditorBlogInfoRepository;
import com.sanshan.dao.elastic.UserInfoRepository;
import com.sanshan.pojo.dto.MarkDownBlogDTO;
import com.sanshan.pojo.dto.UeditorBlogDTO;
import com.sanshan.pojo.elastic.ElasticBaseEditorDO;
import com.sanshan.pojo.elastic.ElasticMarkDownBlogDO;
import com.sanshan.pojo.elastic.ElasticUeditorBlogDO;
import com.sanshan.service.convent.MarkDownEditorConvert;
import com.sanshan.service.convent.UeditorEditorConvert;
import com.sanshan.service.vo.ElasticResponseVO;
import com.sanshan.service.vo.ResponseMsgVO;
import com.sanshan.util.exception.NotFoundBlogException;
import com.sanshan.util.info.EditorTypeEnum;
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
    private ExecutorService pool =  new ThreadPoolExecutor(1,10,3, TimeUnit.MINUTES,new SynchronousQueue<>(),(r)->{
        Thread t = new Thread(r);
        t.setName("es-operation-thread:"+POOL_NUMBER);
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
     * @param key
     * @param responseMsgVO
     */
    public void queryAll(String key,Integer pageRows,Integer pageNum, ResponseMsgVO responseMsgVO){
        SourceFilter sourceFilter =new FetchSourceFilter(new String[]{},new String[]{"content"});
         SearchQuery query = new NativeSearchQueryBuilder().withQuery(matchQuery("_all", key)).withIndices("_all").withPageable(new PageRequest(pageNum,pageRows)).withSourceFilter(sourceFilter).build();
         template.query(query,(searchResponse)->{
           List<ElasticResponseVO> sources = assembleElasticReuturnSourceResponse(searchResponse);
           return responseMsgVO.buildOKWithData(sources);
         });
     }

    /**
     * 用户相关信息查询
     * @param key
     * @param responseMsgVO
     */
     public void userInfoSearch(String key,int pageRows,int pageNum,ResponseMsgVO responseMsgVO){
         SearchQuery query = new NativeSearchQueryBuilder().withQuery(matchQuery("_all",key)).withIndices("user-info").withTypes("user").withPageable(new PageRequest(pageNum,pageRows)).withFields().build();
         template.query(query, (searchResponse) -> {
             List<ElasticResponseVO>  responseVOS= assembleElasticReuturnSourceResponse(searchResponse);
             return responseMsgVO.buildOKWithData(responseVOS);
         });
     }

    /**
     * 用户名查询
     * @param key
     * @param responseMsgVO
     */
    public void userNameSearch(String key, ResponseMsgVO responseMsgVO) {
        responseMsgVO.buildOKWithData(userInfoRepository.findByUsername(key));
    }


    /**
     * 博客信息查询
     * @param key
     * @param responseMsgVO
     */
    public void blogInfoSearch(String key,Integer pageRows,Integer pageNum,ResponseMsgVO responseMsgVO){
        SourceFilter sourceFilter =new FetchSourceFilter(new String[]{},new String[]{"content"});
        SearchQuery query = new NativeSearchQueryBuilder().withQuery(matchQuery("_all",key)).withIndices("blogs").withPageable(new PageRequest(pageNum,pageRows)).withSourceFilter(sourceFilter).build();
        template.query(query, (searchResponse) -> {
            List<ElasticResponseVO> response = assembleElasticReuturnSourceResponse(searchResponse);
            return responseMsgVO.buildOKWithData(response);
        });
    }

    /**
     * 博客内容查询
     * @param key
     * @param responseMsgVO
     */
    public void blogContentSearch(String key, ResponseMsgVO responseMsgVO) {
      List<ElasticMarkDownBlogDO> markdownResult= markDownBlogInfoRepository.findByContent(key);
        List<ElasticUeditorBlogDO> ueditorResult = ueditorBlogInfoRepository.findByContent(key);
        List<ElasticBaseEditorDO> allResult = new LinkedList<>();
        allResult.addAll(markdownResult);
        allResult.addAll(ueditorResult);
        responseMsgVO.buildOKWithData(allResult);
    }


    /**
     *ueditor类型博客数据添加到es
     * @param ueditorBlogDTO
     */
     public void ueditorBlogAdd(UeditorBlogDTO ueditorBlogDTO){
         ElasticUeditorBlogDO elasticUeditorBlogDO = UeditorEditorConvert.dtoToElastic(ueditorBlogDTO);
         ueditorBlogInfoRepository.save(elasticUeditorBlogDO);
     }


    /**
     *markdown类型博客数据添加到es
     * @param markDownBlogDTO
     */
    public void markdownBlogAdd(MarkDownBlogDTO markDownBlogDTO) {
        ElasticMarkDownBlogDO elasticMarkDownBlogDO = MarkDownEditorConvert.dtoToElastic(markDownBlogDTO);
        markDownBlogInfoRepository.save(elasticMarkDownBlogDO);
    }


    /**
     *删除博客在es中的数据
     * @param id
     * @param type
     */
    public void deleteBlog(Long id,EditorTypeEnum type){
        switch (type){
            case MARKDOWN_EDITOR:
                markDownBlogInfoRepository.delete(id);
                break;
            case UEDITOR_EDITOR:
                ueditorBlogInfoRepository.delete(id);
                break;
            case VOID_ID:
                throw new NotFoundBlogException("无法删除 ID已失效");
            default:break;
        }
        log.debug("删除博客id为:{} 在Es中搜索数据完成",id);
    }

    /**
     * 组装返回Source结果
     * @param searchResponse
     * @return
     */
    private List<ElasticResponseVO> assembleElasticReuturnSourceResponse(SearchResponse searchResponse){
        List<ElasticResponseVO> sources = new LinkedList<>();
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.hits();
        for (int i = 0; i < searchHits.length; i++) {
            ElasticResponseVO responseVO = new ElasticResponseVO();
            responseVO.setSource(searchHits[i].getSource());
            responseVO.setId(searchHits[i].getId());
            responseVO.setType(searchHits[i].getType());
            responseVO.setScore(searchHits[i].getScore());
            sources.add(responseVO);
        }
        return  sources;
    }

    /**
     * 组装自定义filed结果
     * @param searchResponse
     * @return
     */
    private List<ElasticResponseVO> assembleElasticReuturnFiledsResponse(SearchResponse searchResponse){
        List<ElasticResponseVO> fileds = new LinkedList<>();
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.hits();
        for (int i = 0; i < searchHits.length; i++) {
            ElasticResponseVO responseVO = new ElasticResponseVO();
            responseVO.setFields(searchHits[i].getFields());
            responseVO.setId(searchHits[i].getId());
            responseVO.setType(searchHits[i].getType());
            responseVO.setScore(searchHits[i].getScore());
            fileds.add(responseVO);
        }
        return  fileds;
    }

}
