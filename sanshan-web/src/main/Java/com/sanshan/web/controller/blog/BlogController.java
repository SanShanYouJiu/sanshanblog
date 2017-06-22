package com.sanshan.web.controller.blog;

import com.sanshan.service.BlogService;
import com.sanshan.service.vo.BlogVO;
import com.sanshan.service.vo.ResponseMsgVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;


@RequestMapping("blog")
@RestController
public class BlogController {


    @Autowired
    BlogService blogService;


    @RequestMapping(value = "query-by-id", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO getBlog(@RequestParam("id") Long id) throws Exception {
        ResponseMsgVO<BlogVO> responseMsgVO = new ResponseMsgVO<>();
        BlogVO blog = blogService.getBlog(id);
        if (Objects.isNull(blog)) {
            throw new NullPointerException("ID已失效");
        }
        return responseMsgVO.buildOKWithData(blog);
    }


    @RequestMapping(value = "query-by-tag",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO<List<BlogVO>> queryByTag(@RequestParam("tag")String tag){
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        List<BlogVO> list = blogService.getBlogByTag(tag);
        return responseMsgVO.buildOKWithData(list);
    }


    @RequestMapping(value = "query-by-title",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO<List<BlogVO>>  queryByTitle(@RequestParam("title")String title){
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        List<BlogVO> list = blogService.getBlogByTitle(title);
        return responseMsgVO.buildOKWithData(list);
    }


    @RequestMapping(value = "query-all",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO queryAllBlog() {
        ResponseMsgVO<List<BlogVO>> responseMsgVO = new ResponseMsgVO();
        List<BlogVO> list = blogService.queryAll();
        return responseMsgVO.buildOKWithData(list);
    }


    @RequestMapping(value = "delete-by-id",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PreAuthorize("hasRole('USER')")
    public ResponseMsgVO blogState(@RequestParam("id") Long id) {
        //id去除
        ResponseMsgVO responseMsgVO= blogService.removeBlog(id);
        return responseMsgVO;
    }

    /**
     * 获得目前的最新博客ID
     * @return 最新博客ID
     */
    @RequestMapping(value = "get-current-id", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PreAuthorize("hasRole('USER')")
    public ResponseMsgVO getCurrentId() {
          Long id =  blogService.getCurrentId();
        return new ResponseMsgVO().buildOKWithData(id);
    }




}
