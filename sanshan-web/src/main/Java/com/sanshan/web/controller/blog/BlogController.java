package com.sanshan.web.controller.blog;

import com.sanshan.service.BlogService;
import com.sanshan.service.vo.BlogVO;
import com.sanshan.service.vo.ResponseMsgVO;
import com.sanshan.util.exception.ERROR;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;


@RequestMapping("blog")
@RestController
@Slf4j
public class BlogController {


    @Autowired
    private BlogService blogService;


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
    public ResponseMsgVO queryByTag(@RequestParam("tag")String tag){
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        List<BlogVO> list = blogService.getBlogByTag(tag);
        if (Objects.isNull(list))
            return responseMsgVO.buildError(new ERROR(404,"无效的标签"));
        return responseMsgVO.buildOKWithData(list);
    }

    @RequestMapping(value = "query-tag-all",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO queryTagAll(){
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
       List list =blogService.queryTagAll();
        if (Objects.isNull(list))
            return responseMsgVO.buildError(new ERROR(404,"没有标签"));
        return responseMsgVO.buildOKWithData(list);
    }

    @RequestMapping(value = "query-by-title",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO  queryByTitle(@RequestParam("title")String title){
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        List<BlogVO> list = blogService.getBlogByTitle(title);
        if (Objects.isNull(list))
            return responseMsgVO.buildError(new ERROR(404, "无效的标题"));
        return responseMsgVO.buildOKWithData(list);
    }

    @RequestMapping(value = "query-title-all",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO queryTitleAll(){
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        List list =blogService.queryTitleAll();
        if (Objects.isNull(list))
            return responseMsgVO.buildError(new ERROR(404,"没有标题"));
        return responseMsgVO.buildOKWithData(list);
    }


    @RequestMapping(value = "query-by-date",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO  queryByDate(@RequestParam("date")String dateString){
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date= null;
        try {
            date = format.parse(dateString);
        } catch (ParseException e) {
           log.error("无法解析字符串{}",dateString);
            e.printStackTrace();
        }
        List<BlogVO> list = blogService.getBlogByDate(date);
        if (Objects.isNull(list))
            return responseMsgVO.buildError(new ERROR(404, "无效的日期"));
        return responseMsgVO.buildOKWithData(list);
    }

    @RequestMapping(value = "query-date-all",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO queryDateAll(){
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        List list =blogService.queryDateAll();
        if (Objects.isNull(list))
            return responseMsgVO.buildError(new ERROR(404,"没有日期"));
      return  responseMsgVO.buildOKWithData(list);
    }


    @RequestMapping(value = "query-all",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO queryAllBlog() {
        ResponseMsgVO<List<BlogVO>> responseMsgVO = new ResponseMsgVO();
        //List<BlogVO> list = blogService.queryAll();
        List<BlogVO> list = blogService.queryAllOfIdMap();
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
    public ResponseMsgVO getCurrentId() {
          Long id =  blogService.getCurrentId();
        return new ResponseMsgVO().buildOKWithData(id);
    }


}
