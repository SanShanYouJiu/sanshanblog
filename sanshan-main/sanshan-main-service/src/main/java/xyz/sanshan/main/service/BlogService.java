package xyz.sanshan.main.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.sanshan.common.BlogIdGenerate;
import xyz.sanshan.common.PageInfo;
import xyz.sanshan.common.exception.MapFoundNullException;
import xyz.sanshan.common.exception.NotFoundBlogException;
import xyz.sanshan.common.info.EditorTypeEnum;
import xyz.sanshan.common.info.PosCodeEnum;
import xyz.sanshan.main.pojo.dto.MarkDownBlogDTO;
import xyz.sanshan.main.pojo.dto.UeditorBlogDTO;
import xyz.sanshan.main.service.editor.MarkDownBlogService;
import xyz.sanshan.main.service.editor.UeditorBlogService;
import xyz.sanshan.main.service.vo.BlogVO;
import xyz.sanshan.common.vo.ResponseMsgVO;

import java.util.*;

/**
 * Service在新增Editor时需要修改
 */
@Service
@Slf4j
public class BlogService {

    @Autowired
    private MarkDownBlogService markDownBlogService;

    @Autowired
    private UeditorBlogService uEditorBlogService;

    @Autowired
    private BlogIdGenerate blogIdGenerate;


    public Long getCurrentId() {
        return blogIdGenerate.getExistMaxId();
    }

    /**
     * 查询对应tag标签的博客
     *
     * @param tag
     * @return
     */
    public List<BlogVO> getBlogByTag(String tag) {
        List<BlogVO> blogVOS = new LinkedList<>();
        Set<Long> longs = blogIdGenerate.getTagMap(tag);
        if (Objects.isNull(longs)){
            return null;
        }
        Long[] a = {};
        Long[] ids = longs.toArray(a);
        Map<Long, String> titleMap = blogIdGenerate.getInvertIdTitleMap();
        Map<Long, EditorTypeEnum> idMap = blogIdGenerate.getIdCopy();
        for (int i = 0; i < ids.length; i++) {
            Long id = ids[i];
            switchTypeAssembleBlogList(id, titleMap, blogVOS, idMap.get(id));
        }
        return blogVOS;
    }

    public List queryTagAll() {
        List list = new LinkedList();
        Map<String, Set<Long>> map = blogIdGenerate.getIdTagCopy();
        for (Map.Entry<String, Set<Long>> entry : map.entrySet()) {
            list.add(entry.getKey());
        }
        return list;
    }

    public PageInfo queryTagByPage(long pageRows,long pageSize){
        List list = new LinkedList();
        PageInfo pageInfo = blogIdGenerate.getIdTagCopyByPage(pageRows, pageSize);
        Map<String, Set<Long>> map = pageInfo.getCurrentMapData();
        for (Map.Entry<String, Set<Long>> entry : map.entrySet()) {
            list.add(entry.getKey());
        }
        pageInfo.setCurrentMapData(null);
        pageInfo.setCompleteData(list);
        return pageInfo;
    }

    public List queryTitleAll() {
        List list = new LinkedList();
        Map<String, Set<Long>> map = blogIdGenerate.getIdTitleCopy();
        for (Map.Entry<String, Set<Long>> entry : map.entrySet()) {
            list.add(entry.getKey());
        }
        return list;
    }

    /**
     * 查询对应title标签的博客
     *
     * @param title
     * @return
     */
    public List<BlogVO> getBlogByTitle(String title) {
        List<BlogVO> blogVOS = new LinkedList<>();
        Set<Long> longs = blogIdGenerate.getTitleMap(title);
        if (Objects.isNull(longs)){
            return null;
        }
        Long[] a = {};
        Long[] ids = longs.toArray(a);
        Map<Long, String> titleMap = blogIdGenerate.getInvertIdTitleMap();
        Map<Long, EditorTypeEnum> idMap = blogIdGenerate.getIdCopy();
        for (int i = 0; i < ids.length; i++) {
            Long id = ids[i];
            switchTypeAssembleBlogList(id, titleMap, blogVOS, idMap.get(id));
        }
        return blogVOS;
    }

    public PageInfo queryTitleByPage(long pageRows, long pageNum) {
        List list = new LinkedList();
        PageInfo pageInfo = blogIdGenerate.getIdTitleByPage(pageRows, pageNum);
        Map<String, Set<Long>> map = pageInfo.getCurrentMapData();
        for (Map.Entry<String, Set<Long>> entry : map.entrySet()) {
            list.add(entry.getKey());
        }
        pageInfo.setCurrentMapData(null);
        pageInfo.setCompleteData(list);
        return pageInfo;
    }

    public List queryDateAll() {
        List list = new LinkedList();
        Map<Date, Set<Long>> map = blogIdGenerate.getIdDateCopy();
        for (Map.Entry<Date, Set<Long>> entry : map.entrySet()) {
            list.add(entry.getKey());
        }
        return list;
    }

    public PageInfo queryDateByPage(long pageRows,long pageNum){
        List list = new LinkedList();
        PageInfo pageInfo = blogIdGenerate.getIdDateCopyByPage(pageRows, pageNum);
        Map<Date, Set<Long>> map = pageInfo.getCurrentMapData();
        for (Map.Entry<Date, Set<Long>> entry : map.entrySet()) {
            list.add(entry.getKey());
        }
        pageInfo.setCurrentMapData(null);
        pageInfo.setCompleteData(list);
        return pageInfo;
    }

    /**
     * 查询对应date标签的博客
     *
     * @param date
     * @return
     */
    public List<BlogVO> getBlogByDate(Date date) {
        List<BlogVO> blogVOS = new LinkedList<>();
        Set<Long> longs = blogIdGenerate.getDateMap(date);
        if (Objects.isNull(longs)){
            return null;
        }
        Long[] a = {};
        Long[] ids = longs.toArray(a);
        Map<Long, String> titleMap = blogIdGenerate.getInvertIdTitleMap();
        Map<Long, EditorTypeEnum> idMap = blogIdGenerate.getIdCopy();
        for (int i = 0; i < ids.length; i++) {
            Long id = ids[i];
            switchTypeAssembleBlogList(id, titleMap, blogVOS, idMap.get(id));
        }
        return blogVOS;
    }


    public BlogVO getBlog(Long id) {
        EditorTypeEnum type = blogIdGenerate.getType(id);
        BlogVO blog = null;
        if (Objects.isNull(type)){
            return blog;
        }
        switch (type) {
            case UEDITOR_EDITOR:
                blog = new BlogVO(uEditorBlogService.queryDtoById(id));
                break;
            case MARKDOWN_EDITOR:
                blog = new BlogVO(markDownBlogService.queryDtoById(id));
                break;
            case VOID_ID:
                break;
            default:
                 break;
        }
        return blog;
    }


    public void removeBlog(Long id,ResponseMsgVO responseMsgVO) {
        EditorTypeEnum type = blogIdGenerate.getType(id);
        log.info("正在删除id为{}的博客",id);
        switch (type) {
            case UEDITOR_EDITOR:
                int result = uEditorBlogService.deleteDOById(id);
                if (result == 0) {
                    responseMsgVO.buildWithMsgAndStatus(PosCodeEnum.INTER_ERROR, "删除对于Id为:" + id + "的博客失败");
                    return;
                }
                break;
            case MARKDOWN_EDITOR:
                int result2 = markDownBlogService.deleteDOById(id);
                if (result2 == 0) {
                    responseMsgVO.buildWithMsgAndStatus(PosCodeEnum.INTER_ERROR, "删除对于Id为:" + id + "的博客失败");
                    return;
                }
                break;
            case VOID_ID:
                throw new NotFoundBlogException("无法删除 ID已失效");
             default:
                 break;
        }
        log.info("删除id为{}的博客成功",id);
        responseMsgVO.buildOK();
    }


    public List<BlogVO> queryAll() {
        List<BlogVO> blogs = new LinkedList<>();
        Long maxId = blogIdGenerate.getExistMaxId();
        for (long i = 0; i < maxId; i++) {
            BlogVO blogVO = getBlog(i);
            if (Objects.isNull(blogVO)) {
                continue;
            } else {
                blogs.add(blogVO);
            }
        }
        return blogs;
    }

    public List<BlogVO> queryAllOfIdMap() {
        List<BlogVO> blogVOS = new LinkedList<>();
        Map<Long, EditorTypeEnum> map = blogIdGenerate.getIdCopy();
        Map<Long, String> titleMap = blogIdGenerate.getInvertIdTitleMap();
        for (Map.Entry<Long, EditorTypeEnum> entry : map.entrySet()) {
            long id = entry.getKey();
            switchTypeAssembleBlogList(id, titleMap, blogVOS, entry.getValue());
        }
        return blogVOS;
    }

    public PageInfo queryByPage(long pageRows, long pageSize) {
        List<BlogVO> blogVOS = new LinkedList<>();
        PageInfo pageInfo = blogIdGenerate.getIdCopyByPage(pageRows, pageSize);
        Map<Long, EditorTypeEnum> map = pageInfo.getCurrentMapData();
        Map<Long, String> titleMap = blogIdGenerate.getInvertIdTitleMap();
        for (Map.Entry<Long, EditorTypeEnum> entry : map.entrySet()) {
            Long id = entry.getKey();
            switchTypeAssembleBlogList(id, titleMap, blogVOS, entry.getValue());
        }
        pageInfo.setCurrentMapData(null);
        pageInfo.setCompleteData(blogVOS);
        return pageInfo;
    }

    protected void switchTypeAssembleBlogList(Long id, Map<Long, String> titleMap, List<BlogVO> blogVOS, EditorTypeEnum type) {
        if (type==null){
            throw new MapFoundNullException();
        }
        switch (type) {
            case MARKDOWN_EDITOR:
                MarkDownBlogDTO m = new MarkDownBlogDTO();
                m.setId(id);
                m.setTitle(titleMap.get(id));
                blogVOS.add(new BlogVO(m));
                break;
            case UEDITOR_EDITOR:
                UeditorBlogDTO u = new UeditorBlogDTO();
                u.setId(id);
                u.setTitle(titleMap.get(id));
                blogVOS.add(new BlogVO(u));
                break;
            case VOID_ID:
                break;
             default:
                 break;
        }
    }

}


