package xyz.sanshan.search.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 *
 */
@Service
@Slf4j
public class ElasticSearchService {

    //private static final AtomicInteger POOL_NUMBER = new AtomicInteger(1);
    //private ExecutorService pool = new ThreadPoolExecutor(1, 10, 3, TimeUnit.MINUTES, new SynchronousQueue<>(), (r) -> {
    //    Thread t = new Thread(r);
    //    t.setName("es-operation-thread:" + POOL_NUMBER);
    //    return t;
    //});
    //
    //@Autowired
    //private ElasticsearchTemplate template;
    //
    //@Autowired
    //private UserInfoRepository userInfoRepository;
    //
    //@Autowired
    //private MarkDownBlogInfoRepository markDownBlogInfoRepository;
    //
    //@Autowired
    //private UeditorBlogInfoRepository ueditorBlogInfoRepository;
    //
    ///**
    // * 全部查询
    // * 前期使用  应该尽量使用按类型搜索
    // * 做返回字段判 不将content这种数据量大的进行返回到response中
    // *
    // * @param key
    // * @param responseMsgVO
    // */
    //public void queryAll(String key, Integer pageRows, Integer pageNum, ResponseMsgVO responseMsgVO) {
    //    //Es默认是从0开始分页 前端默认是从1开始分页 需要注意
    //    pageNum=pageNum-1;
    //    SourceFilter sourceFilter = new FetchSourceFilter(new String[]{}, new String[]{"content"});
    //    SearchQuery query = new NativeSearchQueryBuilder().withQuery(matchQuery("_all", key)).withIndices("_all").withPageable(new PageRequest(pageNum, pageRows)).withSourceFilter(sourceFilter).build();
    //    template.query(query, (searchResponse) -> {
    //        ElasticResponseVO responseVO = assembleElasticReuturnSourceResponse(searchResponse);
    //        return responseMsgVO.buildOKWithData(responseVO);
    //    });
    //}
    //
    ///**
    // * 用户相关信息查询
    // *
    // * @param key
    // * @param responseMsgVO
    // */
    //public void userInfoSearch(String key, int pageRows, int pageNum, ResponseMsgVO responseMsgVO) {
    //    pageNum=pageNum-1;
    //    SearchQuery query = new NativeSearchQueryBuilder().withQuery(matchQuery("_all", key)).withIndices("user-info").withTypes("user").withPageable(new PageRequest(pageNum, pageRows)).withFields().build();
    //    template.query(query, (searchResponse) -> {
    //        ElasticResponseVO responseVO = assembleElasticReuturnSourceResponse(searchResponse);
    //        return responseMsgVO.buildOKWithData(responseVO);
    //    });
    //}
    //
    ///**
    // * 用户名查询
    // *
    // * @param key
    // * @param responseMsgVO
    // */
    //public void userNameSearch(String key, ResponseMsgVO responseMsgVO) {
    //    responseMsgVO.buildOKWithData(userInfoRepository.findByUsername(key));
    //}
    //
    //
    ///**
    // * 博客信息查询
    // *
    // * @param key
    // * @param responseMsgVO
    // */
    //public void blogInfoSearch(String key, Integer pageRows, Integer pageNum, ResponseMsgVO responseMsgVO) {
    //    pageNum=pageNum-1;
    //    SourceFilter sourceFilter = new FetchSourceFilter(new String[]{}, new String[]{"content"});
    //    SearchQuery query = new NativeSearchQueryBuilder().withQuery(matchQuery("_all", key)).withIndices("blogs").withPageable(new PageRequest(pageNum, pageRows)).withSourceFilter(sourceFilter).build();
    //    template.query(query, (searchResponse) -> {
    //        ElasticResponseVO responseVO = assembleElasticReuturnSourceResponse(searchResponse);
    //        return responseMsgVO.buildOKWithData(responseVO);
    //    });
    //}
    //
    ///**
    // * 博客内容查询
    // *
    // * @param key
    // * @param responseMsgVO
    // */
    //public void blogContentSearch(String key, ResponseMsgVO responseMsgVO) {
    //    List<ElasticMarkDownBlogDO> markdownResult = markDownBlogInfoRepository.findByContent(key);
    //    List<ElasticUeditorBlogDO> ueditorResult = ueditorBlogInfoRepository.findByContent(key);
    //    List<ElasticBaseEditorDO> allResult = new LinkedList<>();
    //    allResult.addAll(markdownResult);
    //    allResult.addAll(ueditorResult);
    //    responseMsgVO.buildOKWithData(allResult);
    //}
    //
    //
    ///**
    // * ueditor类型博客数据添加到es
    // *
    // * @param ueditorBlogDTO
    // */
    //public void ueditorBlogAdd(UeditorBlogDTO ueditorBlogDTO) {
    //    ElasticUeditorBlogDO elasticUeditorBlogDO = UeditorEditorConvert.dtoToElastic(ueditorBlogDTO);
    //    ueditorBlogInfoRepository.save(elasticUeditorBlogDO);
    //}
    //
    //
    ///**
    // * markdown类型博客数据添加到es
    // *
    // * @param markDownBlogDTO
    // */
    //public void markdownBlogAdd(MarkDownBlogDTO markDownBlogDTO) {
    //    ElasticMarkDownBlogDO elasticMarkDownBlogDO = MarkDownEditorConvert.dtoToElastic(markDownBlogDTO);
    //    markDownBlogInfoRepository.save(elasticMarkDownBlogDO);
    //}
    //
    ///**
    // * 用户数据添加到ES
    // */
    //public Boolean userAdd(UserDTO userDTO){
    //    ElasticUserDO elasticUserDO = UserConvert.dtoToElasticDO(userDTO);
    //    return    userInfoRepository.save(elasticUserDO)!=null? true:false;
    //}
    //
    ///**
    // * 删除博客在es中的数据
    // *
    // * @param id
    // * @param type
    // */
    //public void deleteBlog(Long id, EditorTypeEnum type) {
    //    switch (type) {
    //        case MARKDOWN_EDITOR:
    //            markDownBlogInfoRepository.delete(id);
    //            break;
    //        case UEDITOR_EDITOR:
    //            ueditorBlogInfoRepository.delete(id);
    //            break;
    //        case VOID_ID:
    //            throw new NotFoundBlogException("无法删除 ID已失效");
    //        default:
    //            break;
    //    }
    //    log.debug("删除博客id为:{} 在Es中搜索数据完成", id);
    //}
    //
    ///**
    // * 组装返回Source结果
    // *
    // * @param searchResponse
    // * @return
    // */
    //private ElasticResponseVO assembleElasticReuturnSourceResponse(SearchResponse searchResponse) {
    //    ElasticResponseVO responseVO = new ElasticResponseVO();
    //    List<ElasticSearchResultDTO> sources = new LinkedList<>();
    //    SearchHits hits = searchResponse.getHits();
    //    SearchHit[] searchHits = hits.hits();
    //    for (int i = 0; i < searchHits.length; i++) {
    //        ElasticSearchResultDTO hit = new ElasticSearchResultDTO();
    //        hit.setSource(searchHits[i].getSource());
    //        hit.setId(searchHits[i].getId());
    //        hit.setType(searchHits[i].getType());
    //        hit.setScore(searchHits[i].getScore());
    //        sources.add(hit);
    //    }
    //    responseVO.setTotal(hits.getTotalHits());
    //    responseVO.setResult(sources);
    //    return responseVO;
    //}
    //
    ///**
    // * 组装自定义filed结果
    // *
    // * @param searchResponse
    // * @return
    // */
    //private ElasticResponseVO assembleElasticReuturnFiledsResponse(SearchResponse searchResponse) {
    //    ElasticResponseVO responseVO = new ElasticResponseVO();
    //    List<ElasticSearchResultDTO> fileds = new LinkedList<>();
    //    SearchHits hits = searchResponse.getHits();
    //    SearchHit[] searchHits = hits.hits();
    //    for (int i = 0; i < searchHits.length; i++) {
    //        ElasticSearchResultDTO hit = new ElasticSearchResultDTO();
    //        hit.setFields(searchHits[i].getFields());
    //        hit.setId(searchHits[i].getId());
    //        hit.setType(searchHits[i].getType());
    //        hit.setScore(searchHits[i].getScore());
    //        fileds.add(hit);
    //    }
    //    responseVO.setTotal(hits.getTotalHits());
    //    responseVO.setResult(fileds);
    //    return responseVO;
    //}


}
