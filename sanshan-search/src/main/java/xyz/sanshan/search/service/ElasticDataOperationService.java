package xyz.sanshan.search.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.sanshan.search.convert.MarkDownEditorConvert;
import xyz.sanshan.search.convert.UeditorEditorConvert;
import xyz.sanshan.search.convert.UserConvert;
import xyz.sanshan.search.dao.MarkDownBlogInfoRepository;
import xyz.sanshan.search.dao.UeditorBlogInfoRepository;
import xyz.sanshan.search.dao.UserInfoRepository;
import xyz.sanshan.search.pojo.DO.ElasticMarkDownBlogDO;
import xyz.sanshan.search.pojo.DO.ElasticUeditorBlogDO;
import xyz.sanshan.search.pojo.DO.ElasticUserDO;
import xyz.sanshan.search.pojo.DTO.MarkDownBlogDTO;
import xyz.sanshan.search.pojo.DTO.UeditorBlogDTO;
import xyz.sanshan.search.pojo.DTO.UserDTO;

@Service
@Slf4j
public class ElasticDataOperationService {


    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private MarkDownBlogInfoRepository markDownBlogInfoRepository;

    @Autowired
    private UeditorBlogInfoRepository ueditorBlogInfoRepository;


    /**
     * ueditor类型博客数据添加到es
     *
     * @param ueditorBlogDTO
     */
    public void ueditorBlogAdd(UeditorBlogDTO ueditorBlogDTO) {
        ElasticUeditorBlogDO elasticUeditorBlogDO = UeditorEditorConvert.dtoToElastic(ueditorBlogDTO);
        ueditorBlogInfoRepository.save(elasticUeditorBlogDO);
    }


    /**
     * markdown类型博客数据添加到es
     *
     * @param markDownBlogDTO
     */
    public void markdownBlogAdd(MarkDownBlogDTO markDownBlogDTO) {
        ElasticMarkDownBlogDO elasticMarkDownBlogDO = MarkDownEditorConvert.dtoToElastic(markDownBlogDTO);
        markDownBlogInfoRepository.save(elasticMarkDownBlogDO);
    }

    /**
     * 用户数据添加到ES
     */
    public Boolean userAdd(UserDTO userDTO){
        ElasticUserDO elasticUserDO = UserConvert.dtoToElasticDO(userDTO);
        return    userInfoRepository.save(elasticUserDO)!=null? true:false;
    }

    /**
     * 删除用户
     * @param id
     */
    public void  userDelete(String id){
        userInfoRepository.delete(id);
    }

    /**
     * 删除markdown博客
     * @param id
     */
    public void markdownDelete(Long id){
        markDownBlogInfoRepository.delete(id);
    }

    /**
     * 删除ueditor博客
     * @param id
     */
    public void ueditorDelete(Long id){
        ueditorBlogInfoRepository.delete(id);
    }

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


}
