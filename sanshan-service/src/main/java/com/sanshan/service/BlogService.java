package com.sanshan.service;

import com.sanshan.pojo.dto.MarkDownBlogDTO;
import com.sanshan.pojo.dto.UEditorBlogDTO;
import com.sanshan.service.editor.MarkDownBlogService;
import com.sanshan.service.editor.UeditorBlogService;
import com.sanshan.service.vo.BlogVO;
import com.sanshan.service.vo.ResponseMsgVO;
import com.sanshan.util.BlogIdGenerate;
import com.sanshan.util.exception.ERROR;
import com.sanshan.util.info.EditorTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Service在新增Editor时需要修改
 */
@Service
@Slf4j
public class BlogService {
    @Autowired
    MarkDownBlogService markDownBlogService;

    @Autowired
    UeditorBlogService uEditorBlogService;

    @Autowired
    BlogIdGenerate blogIdGenerate;


    public Long getCurrentId() {
        Long id = blogIdGenerate.getSize();
        for (long i = id-1; i > -1; i--) {
            EditorTypeEnum type = blogIdGenerate.getType(i);
            switch (type) {
                case UEDITOR_EDITOR:
                    return i;
                case MarkDown_EDITOR:
                    return i;
                case Void_Id:
                    continue;
            }
        }
        log.error("获取当前ID错误:{}", id);
        throw new NullPointerException("未知错误");
    }

    public BlogVO getBlog(Long id) {
        EditorTypeEnum type = blogIdGenerate.getType(id);
        BlogVO blog = null;
        switch (type) {
            case UEDITOR_EDITOR:
                blog = new BlogVO(uEditorBlogService.queryDtoById(id));
                break;
            case MarkDown_EDITOR:
                blog = new BlogVO(markDownBlogService.queryDtoById(id));
                break;
            case Void_Id:
                break;
        }
        return blog;
    }


    /**
     * 查询对应tag标签的博客
     * @param tag
     * @return
     */
    public List<BlogVO> getBlogByTag(String tag) {
        List<BlogVO> blogVOS = new LinkedList<>();
        Set<Long> longs = blogIdGenerate.getTagMap(tag);
        if (Objects.isNull(longs))
            return  null;
        Long[] a = {};
        Long[] ids = longs.toArray(a);
        for (long i = 0; i < ids.length; i++) {
            BlogVO blogVO = getBlog(ids[(int) i]);
            blogVOS.add(blogVO);
        }
        return blogVOS;
    }

    public List queryTagAll() {
        List list = new LinkedList();
        Map<String,Set<Long>> map = blogIdGenerate.getIdTagCopy();
        for (Map.Entry<String, Set<Long>> entry : map.entrySet()) {
            list.add(entry.getKey());
        }
        return list;
    }

    public List queryTitleAll() {
        List list = new LinkedList();
        Map<String,Set<Long>> map = blogIdGenerate.getIdTitleCopy();
        for (Map.Entry<String, Set<Long>> entry : map.entrySet()) {
            list.add(entry.getKey());
        }
        return list;
    }



    /**
     * 查询对应title标签的博客
     * @param title
     * @return
     */
    public List<BlogVO> getBlogByTitle(String title) {
        List<BlogVO> blogVOS = new LinkedList<>();
        Set<Long> longs = blogIdGenerate.getTitleMap(title);
        if (Objects.isNull(longs))
            return null;
        Long[] a={};
        Long[] ids = longs.toArray(a);
        for (long i = 0; i <ids.length ; i++) {
            BlogVO blogVO = getBlog(ids[Math.toIntExact(i)]);
            blogVOS.add(blogVO);
        }
        return blogVOS;
    }



    public List queryDateAll() {
        List list = new LinkedList();
        Map<Date,Set<Long>> map = blogIdGenerate.getIdDateCopy();
        for (Map.Entry<Date, Set<Long>> entry : map.entrySet()) {
            list.add(entry.getKey());
        }
        return list;
    }

    /**
     * 查询对应date标签的博客
     * @param date
     * @return
     */
    public List<BlogVO> getBlogByDate(Date date) {
        List<BlogVO> blogVOS = new LinkedList<>();
        Set<Long> longs = blogIdGenerate.getDateMap(date);
        if (Objects.isNull(longs))
            return null;
        Long[] a={};
        Long[] ids = longs.toArray(a);
        for (long i = 0; i <ids.length ; i++) {
            BlogVO blogVO = getBlog(ids[Math.toIntExact(i)]);
            blogVOS.add(blogVO);
        }
        return blogVOS;
    }


    public ResponseMsgVO removeBlog(Long id) {
        EditorTypeEnum type = blogIdGenerate.getType(id);
        blogIdGenerate.remove(id);
        BlogVO blog = null;
        switch (type) {
            case UEDITOR_EDITOR:
               int result= uEditorBlogService.deleteDOById(id);
                if (result==0)
                    return new ResponseMsgVO().buildError(new ERROR(500, "删除对应Id为：" + id + "的博客失败"));
                break;
            case MarkDown_EDITOR:
                int result2 = markDownBlogService.deleteDOById(id);
                if (result2 == 0) {
                    return new ResponseMsgVO().buildError(new ERROR(500, "删除对应Id为：" + id + "的博客失败"));
                }
                break;
            case Void_Id:
                throw new NullPointerException("无法删除 ID已失效");
        }
        return new ResponseMsgVO().buildOK();
    }


    public List<BlogVO> queryAll() {
        List<BlogVO> blogs = new LinkedList<>();
        Long size = blogIdGenerate.getSize();
        for (long i = 0; i <size; i++) {
            BlogVO blogVO=getBlog(i);
            if (Objects.isNull(blogVO)) {
                continue;
            } else {
                blogs.add(blogVO);
            }
        }
        return blogs;
    }

    public  List<BlogVO> queryAllofIdMap(){
        List<BlogVO> blogVOS = new LinkedList<>();
        Map<Long,EditorTypeEnum>  map = blogIdGenerate.getIdCopy();
        Map<Long, String> titleMap = blogIdGenerate.getInvertIdTitleMap();
        for (Map.Entry<Long, EditorTypeEnum> entry : map.entrySet()) {
            long id= entry.getKey();
            switch (entry.getValue()){
                case UEDITOR_EDITOR:
                    UEditorBlogDTO u = new UEditorBlogDTO();
                    u.setId(id);
                    u.setTitle(titleMap.get(id));
                    blogVOS.add(new BlogVO(u));
                    break;
                case MarkDown_EDITOR:
                    MarkDownBlogDTO m = new MarkDownBlogDTO();
                    m.setId(id);
                    m.setTitle(titleMap.get(id));
                    blogVOS.add(new BlogVO(m));
                    break;
                case Void_Id:
                    continue;
            }
        }

        return blogVOS;
    }

}


