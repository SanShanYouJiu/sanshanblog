package xyz.sanshan.main.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.sanshan.common.PageInfo;
import xyz.sanshan.common.UserContextHandler;
import xyz.sanshan.common.exception.MapFoundNullException;
import xyz.sanshan.common.exception.NotFoundBlogException;
import xyz.sanshan.common.info.EditorTypeEnum;
import xyz.sanshan.common.info.PosCodeEnum;
import xyz.sanshan.common.vo.ResponseMsgVO;
import xyz.sanshan.main.dao.mybatis.MarkDownBlogMapper;
import xyz.sanshan.main.dao.mybatis.UeditorBlogMapper;
import xyz.sanshan.main.pojo.dto.MarkDownBlogDTO;
import xyz.sanshan.main.pojo.dto.UeditorBlogDTO;
import xyz.sanshan.main.pojo.entity.MarkDownBlogDO;
import xyz.sanshan.main.pojo.entity.UeditorBlogDO;
import xyz.sanshan.main.service.convent.BlogConvert;
import xyz.sanshan.main.service.convent.MarkDownEditorConvert;
import xyz.sanshan.main.service.convent.UeditorEditorConvert;
import xyz.sanshan.main.service.editor.BlogIdGenerate;
import xyz.sanshan.main.service.editor.BlogResourcesOperation;
import xyz.sanshan.main.service.editor.MarkDownBlogService;
import xyz.sanshan.main.service.editor.UeditorBlogService;
import xyz.sanshan.main.service.util.EHCacheUtil;
import xyz.sanshan.main.service.vo.BlogVO;

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
    private MarkDownBlogMapper markDownBlogMapper;

    @Autowired
    private UeditorBlogMapper ueditorBlogMapper;


    @Autowired
    private BlogIdGenerate blogIdGenerate;

    @Autowired
    private BlogResourcesOperation blogOperation;

    static {
        EHCacheUtil.initCacheManager();
        EHCacheUtil.initCache("blog_meta_data");
    }

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
        String cacheKey = "BlogService#getBlogByTag:{"+"tag:" + tag +"}";
        if (EHCacheUtil.get(cacheKey) != null) {
            return (List<BlogVO>) EHCacheUtil.get(cacheKey);
        }
        List<BlogVO> blogVOS ;
        blogVOS = buildBlogVOS(ueditorBlogMapper.queryByTag(tag), markDownBlogMapper.queryByTag(tag));
        EHCacheUtil.put(cacheKey,blogVOS);
        return blogVOS;
    }


    /**
     * 其实只是查最近100条
     * @return
     */
    @Deprecated
    public PageInfo queryTagAll() {
        PageInfo pageInfo = queryTagByPage(100, 1);
        return pageInfo;
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


    /**
     * 查询对应title标签的博客
     *
     * @param title
     * @return
     */
    public List<BlogVO> getBlogByTitle(String title) {
        String cacheKey = "BlogService#getBlogByTitle:{"+"title:" + title +"}";
        if (EHCacheUtil.get(cacheKey) != null) {
            return (List<BlogVO>) EHCacheUtil.get(cacheKey);
        }
        List<BlogVO> blogVOS ;
        blogVOS = buildBlogVOS(ueditorBlogMapper.queryByTitle(title), markDownBlogMapper.queryByTitle(title));
        EHCacheUtil.put(cacheKey, blogVOS);
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
        //获得当前用户
        String username= UserContextHandler.getUsername();
        //权限检查
        blogOperation.baseDeleteCheck(id, username);

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


    /**
     * 分页获取首页博客信息
     * @param pageRows
     * @param pageSize
     * @return
     */
    public PageInfo queryByPage(long pageRows, long pageSize) {
        String cacheKey = "BlogService#queryByPage:{"+"pageRows:" + pageRows + "pageSize:" + pageSize+"}";
        if (EHCacheUtil.get(cacheKey)!=null) {
            return (PageInfo) EHCacheUtil.get(cacheKey);
        }
        PageInfo pageInfo = blogIdGenerate.getIdCopyByPage(pageRows, pageSize);
        //build博客相关信息
        PageInfo resultPageInfo = buildHomeBlogInfoPageInfo(pageInfo);
        EHCacheUtil.put(cacheKey, resultPageInfo);
        return resultPageInfo;
    }

    private PageInfo buildHomeBlogInfoPageInfo(PageInfo pageInfo) {
        Map<Long, EditorTypeEnum> idMap = pageInfo.getCurrentMapData();

        List<BlogVO> commonBlogDTOS = new ArrayList<>(idMap.size());
        for (Map.Entry<Long, EditorTypeEnum> map : idMap.entrySet()) {
            switch (map.getValue()) {
                case MARKDOWN_EDITOR:
                   commonBlogDTOS.add( BlogConvert.markdownDTOConvertBlogVO(markDownBlogService.queryDtoById(map.getKey())));
                   break;
                case UEDITOR_EDITOR:
                    commonBlogDTOS.add(BlogConvert.ueditorDTOConvertBlogVO(uEditorBlogService.queryDtoById(map.getKey())));
                    break;
                case VOID_ID:
                    continue;
                default:
                    continue;
            }
        }
        pageInfo.setCurrentMapData(null);
        pageInfo.setCompleteData(commonBlogDTOS);
        return pageInfo;
    }

    private List<BlogVO> buildBlogVOS(List<UeditorBlogDO> ueditorBlogDOS, List<MarkDownBlogDO> markDownBlogDOS) {
        List<BlogVO> blogVOS = new LinkedList<>();
        blogVOS.addAll(BlogConvert.ueditorDoToDtoList(UeditorEditorConvert.doToDtoList(ueditorBlogDOS)));
        blogVOS.addAll(BlogConvert.markdownDoToDtoList(MarkDownEditorConvert.doToDtoList(markDownBlogDOS)));
        return blogVOS;
    }

    @Deprecated
    private void switchTypeAssembleBlogList(Long id, Map<Long, String> titleMap, List<BlogVO> blogVOS, EditorTypeEnum type) {
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


