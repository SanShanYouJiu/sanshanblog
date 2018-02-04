package com.sanshan.service.search;

import com.sanshan.dao.elastic.MarkDownBlogInfoRepository;
import com.sanshan.dao.elastic.UeditorBlogInfoRepository;
import com.sanshan.dao.elastic.UserInfoRepository;
import com.sanshan.pojo.dto.MarkDownBlogDTO;
import com.sanshan.pojo.dto.UeditorBlogDTO;
import com.sanshan.pojo.elastic.ElasticMarkDownBlogDO;
import com.sanshan.pojo.elastic.ElasticUeditorBlogDO;
import com.sanshan.service.convent.MarkDownEditorConvert;
import com.sanshan.service.convent.UeditorEditorConvert;
import com.sanshan.service.vo.ResponseMsgVO;
import com.sanshan.util.exception.NotFoundBlogException;
import com.sanshan.util.info.EditorTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

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

     public void queryAll(String key, ResponseMsgVO responseMsgVO){

     }



     public void ueditorBlogAdd(UeditorBlogDTO ueditorBlogDTO){
         ElasticUeditorBlogDO elasticUeditorBlogDO = UeditorEditorConvert.dtoToElastic(ueditorBlogDTO);
         ueditorBlogInfoRepository.save(elasticUeditorBlogDO);
     }


    public void markdownBlogAdd(MarkDownBlogDTO markDownBlogDTO) {
        ElasticMarkDownBlogDO elasticMarkDownBlogDO = MarkDownEditorConvert.dtoToElastic(markDownBlogDTO);
        markDownBlogInfoRepository.save(elasticMarkDownBlogDO);
    }


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

}
