package xyz.sanshan.main.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.sanshan.common.PageInfo;
import xyz.sanshan.common.UserContextHandler;
import xyz.sanshan.common.exception.NotFoundBlogException;
import xyz.sanshan.common.info.EditorTypeEnum;
import xyz.sanshan.common.info.PosCodeEnum;
import xyz.sanshan.common.vo.ResponseMsgVO;
import xyz.sanshan.main.dao.mybatis.MarkDownBlogMapper;
import xyz.sanshan.main.dao.mybatis.UEditorBlogMapper;
import xyz.sanshan.main.pojo.dto.BaseBlogDTO;
import xyz.sanshan.main.pojo.dto.MarkDownBlogDTO;
import xyz.sanshan.main.pojo.dto.UEditorBlogDTO;
import xyz.sanshan.main.pojo.entity.MarkDownBlogDO;
import xyz.sanshan.main.pojo.entity.UEditorBlogDO;
import xyz.sanshan.main.service.convent.BlogConvert;
import xyz.sanshan.main.service.convent.MarkDownEditorConvert;
import xyz.sanshan.main.service.convent.UEditorEditorConvert;
import xyz.sanshan.main.service.editor.BlogIdGenerate;
import xyz.sanshan.main.service.editor.BlogResourcesOperation;
import xyz.sanshan.main.service.editor.MarkDownBlogService;
import xyz.sanshan.main.service.editor.UEditorBlogService;
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
    private UEditorBlogService uEditorBlogService;

    @Autowired
    private MarkDownBlogMapper markDownBlogMapper;

    @Autowired
    private UEditorBlogMapper UEditorBlogMapper;

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
        return baseQueryElement(cacheKey, UEditorBlogMapper.queryByTag(tag), markDownBlogMapper.queryByTag(tag));
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

    /**
     * 这里的tag可能为空 需要不为空的tag才能往下走
     * 所以需要手动控制分页中的数据判断
     * @param pageRows
     * @param pageNum
     * @return
     */
    public PageInfo queryTagByPage(long pageRows,long pageNum){
        String cacheKey = "BlogService#queryTagByPage:{"+"pageRows:" + pageRows + "pageNum:" + pageNum+"}";
        if (EHCacheUtil.get(cacheKey) != null) {
            return (PageInfo) EHCacheUtil.get(cacheKey);
        }

        long preRows = pageRows * (pageNum - 1);
        long endRows = preRows + pageRows;
        long count = 0;
        TreeMap<Long, EditorTypeEnum> idMap  =  blogIdGenerate.getIdCopy();

        List<BlogVO> blogVOS = new LinkedList();
        for (Map.Entry<Long,EditorTypeEnum> entry: idMap.entrySet()) {
            if (count >= preRows && count<endRows) {
                switch (entry.getValue()) {
                    case MARKDOWN_EDITOR:
                        MarkDownBlogDTO markDownBlogDTO = markDownBlogService.queryDtoById(entry.getKey());
                        if (StringUtils.isBlank(markDownBlogDTO.getTag())) {
                            break;
                        }
                        BlogVO markdownDTOConvertBlogVO = BlogConvert.markdownDTOConvertBlogVO(markDownBlogDTO);
                        markdownDTOConvertBlogVO.setContent(null);
                        blogVOS.add(markdownDTOConvertBlogVO);
                        break;
                    case UEDITOR_EDITOR:
                        UEditorBlogDTO UEditorBlogDTO = uEditorBlogService.queryDtoById(entry.getKey());
                        if (StringUtils.isBlank(UEditorBlogDTO.getTag())) {
                            break;
                        }
                        BlogVO ueditorDTOConvertBlogVO = BlogConvert.ueditorDTOConvertBlogVO(UEditorBlogDTO);
                        ueditorDTOConvertBlogVO.setContent(null);
                        blogVOS.add(ueditorDTOConvertBlogVO);
                        break;
                    case VOID_ID:
                        break;
                    default:
                        break;

                }
            }
            count++;
            if (count==endRows || idMap.lastKey().equals(entry.getKey())) {
                PageInfo pageInfo =  new PageInfo(blogVOS, pageRows, pageNum, idMap.size());
                EHCacheUtil.put(cacheKey,pageInfo);
                return pageInfo;
            }

        }
        return new PageInfo();
    }



    /**
     * 查询对应title标签的博客
     *
     * @param title
     * @return
     */
    public List<BlogVO> getBlogByTitle(String title) {
        String cacheKey = "BlogService#getBlogByTitle:{"+"title:" + title +"}";
        return baseQueryElement(cacheKey, UEditorBlogMapper.queryByTitle(title), markDownBlogMapper.queryByTitle(title));
    }

    public PageInfo queryTitleByPage(long pageRows, long pageNum) {
        String cacheKey = "BlogService#queryTitleByPage:{"+"pageRows:" + pageRows + "pageNum:" + pageNum+"}";
        return baseQueryByPage(cacheKey,pageRows,pageNum);
    }


    public PageInfo queryDateByPage(long pageRows,long pageNum){
        String cacheKey = "BlogService#queryDateByPage:{"+"pageRows:" + pageRows + "pageNum:" + pageNum+"}";
        return baseQueryByPage(cacheKey,pageRows,pageNum);
    }

    /**
     * 查询对应date标签的博客
     *
     * @param date
     * @return
     */
    public List<BlogVO> getBlogByDate(Date date) {
        String cacheKey = "BlogService#getBlogByDate:{"+"date:" + date +"}";
        return baseQueryElement(cacheKey, UEditorBlogMapper.queryByDate(date), markDownBlogMapper.queryByDate(date));
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
     * @param pageNum
     * @return
     */
    public PageInfo queryByPage(long pageRows, long pageNum) {
        String cacheKey = "BlogService#queryByPage:{"+"pageRows:" + pageRows + "pageNum:" + pageNum+"}";
        return baseQueryByPage(cacheKey,pageRows,pageNum);
    }

    private PageInfo baseQueryByPage(String cacheKey, long pageRows, long pageNum) {
        if (EHCacheUtil.get(cacheKey)!=null) {
            return (PageInfo) EHCacheUtil.get(cacheKey);
        }
        PageInfo pageInfo = blogIdGenerate.getIdCopyByPage(pageRows, pageNum);
        //build博客相关信息
        PageInfo resultPageInfo = buildHomeBlogInfoPageInfo(pageInfo);
        EHCacheUtil.put(cacheKey, resultPageInfo);
        return resultPageInfo;
    }


    private List<BlogVO> baseQueryElement(String cacheKey, List<UEditorBlogDO> UEditorBlogDOS, List<MarkDownBlogDO> markDownBlogDOS){
        if (EHCacheUtil.get(cacheKey) != null) {
            return (List<BlogVO>) EHCacheUtil.get(cacheKey);
        }
        List<BlogVO> blogVOS ;
        blogVOS = buildBlogVOS(UEditorBlogDOS, markDownBlogDOS);
        EHCacheUtil.put(cacheKey, blogVOS);
        return  blogVOS;
    }

    private PageInfo buildHomeBlogInfoPageInfo(PageInfo pageInfo) {
        Map<Long, EditorTypeEnum> idMap = pageInfo.getCurrentMapData();

        List<BlogVO> blogVOS = new ArrayList<>(idMap.size());
        for (Map.Entry<Long, EditorTypeEnum> map : idMap.entrySet()) {
            buildBlogBaseInfoS(blogVOS, map);
        }
        pageInfo.setCurrentMapData(null);
        pageInfo.setCompleteData(blogVOS);
        return pageInfo;
    }


    private void buildBlogBaseInfoS(List<BlogVO> blogVOS, Map.Entry<Long, EditorTypeEnum> map) {
        switch (map.getValue()) {
            case MARKDOWN_EDITOR:
                BlogVO markdownBlogVo = BlogConvert.markdownDTOConvertBlogVO(markDownBlogService.queryDtoById(map.getKey()));
                markdownBlogVo.setContent(null);
                blogVOS.add(markdownBlogVo);
               break;
            case UEDITOR_EDITOR:
                BlogVO ueditorBlogVO = BlogConvert.ueditorDTOConvertBlogVO(uEditorBlogService.queryDtoById(map.getKey()));
                ueditorBlogVO.setContent(null);
                blogVOS.add(ueditorBlogVO);
                break;
            case VOID_ID:
                break;
            default:
                break;
        }
    }

    private BlogVO buildBlogVO(BaseBlogDTO baseBlogDTO) {
        if (baseBlogDTO instanceof MarkDownBlogDTO) {
        }else if (baseBlogDTO instanceof UEditorBlogDTO) {

        }
        return null;
    }

    private List<BlogVO> buildBlogVOS(List<UEditorBlogDO> UEditorBlogDOS, List<MarkDownBlogDO> markDownBlogDOS) {
        List<BlogVO> blogVOS = new LinkedList<>();
        blogVOS.addAll(BlogConvert.ueditorDoToDtoList(UEditorEditorConvert.doToDtoList(UEditorBlogDOS)));
        blogVOS.addAll(BlogConvert.markdownDoToDtoList(MarkDownEditorConvert.doToDtoList(markDownBlogDOS)));
        return blogVOS;
    }

}


