package xyz.sanshan.main.service.search;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.sanshan.common.exception.NotFoundBlogException;
import xyz.sanshan.common.info.EditorTypeEnum;
import xyz.sanshan.main.pojo.dto.MarkDownBlogDTO;
import xyz.sanshan.main.pojo.dto.UEditorBlogDTO;
import xyz.sanshan.main.pojo.dto.UserDTO;
import xyz.sanshan.main.service.feign.SearchClient;


/**
 * 由ES与数据库同步进行数据更新 删除
 */
@Service
@Slf4j
@Deprecated
public class ElasticSearchService {

   @Autowired
   private SearchClient searchClient;

    /**
     * ueditor类型博客数据添加到es
     *
     * @param UEditorBlogDTO
     */
    public void ueditorBlogAdd(UEditorBlogDTO UEditorBlogDTO) {
        searchClient.ueditorBlogAdd(UEditorBlogDTO);
    }


    /**
     * markdown类型博客数据添加到es
     *
     * @param markDownBlogDTO
     */
    public void markdownBlogAdd(MarkDownBlogDTO markDownBlogDTO) {
        searchClient.markdownBlogAdd(markDownBlogDTO);
    }

    /**
     * 用户数据添加到ES
     */
    public Boolean userAdd(UserDTO userDTO) {
        searchClient.userAdd(userDTO);
        return true;
    }

    /**
     * 删除博客在es中的数据
     *
     * @param id
     * @param type
     */
    public void deleteBlog(Long id, EditorTypeEnum type) {
        switch (type) {
            case MARKDOWN_EDITOR:
                searchClient.markdownBlogDelete(id);
                break;
            case UEDITOR_EDITOR:
                searchClient.ueditorBlogDelete(id);
                break;
            case VOID_ID:
                throw new NotFoundBlogException("无法删除 ID已失效");
            default:
                break;
        }
        log.debug("删除博客id为:{} 在Es中搜索数据完成", id);
    }

}
