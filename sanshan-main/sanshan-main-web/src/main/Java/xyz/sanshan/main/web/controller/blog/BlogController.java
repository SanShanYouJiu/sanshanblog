package xyz.sanshan.main.web.controller.blog;

import xyz.sanshan.main.service.BlogService;
import xyz.sanshan.main.service.vo.BlogVO;
import xyz.sanshan.common.vo.ResponseMsgVO;
import xyz.sanshan.common.PageInfo;
import xyz.sanshan.common.exception.NotFoundBlogException;
import xyz.sanshan.common.info.PosCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;


@RequestMapping("blog")
@RestController
public class BlogController {


    @Autowired
    private BlogService blogService;


    @GetMapping(value = "id/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO getBlog(@PathVariable("id") Long id) throws Exception {
        ResponseMsgVO<BlogVO> responseMsgVO = new ResponseMsgVO<>();
        BlogVO blog = blogService.getBlog(id);
        if (Objects.isNull(blog)) {
             throw   new NotFoundBlogException("该ID无对应博客");
        }
        return responseMsgVO.buildOKWithData(blog);
    }


    @GetMapping(value = "tag/{tag}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO queryByTag(@PathVariable("tag")String tag){
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        List<BlogVO> list = blogService.getBlogByTag(tag);
        if (Objects.isNull(list)){
            return responseMsgVO.buildWithMsgAndStatus(PosCodeEnum.NOT_FOUND,"未知的标签");
        }
        return responseMsgVO.buildOKWithData(list);
    }

    @GetMapping(value = "tag-all",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO queryTagAll(){
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        List list =blogService.queryTagAll();
        if (Objects.isNull(list)){
            return responseMsgVO.buildWithMsgAndStatus(PosCodeEnum.NOT_FOUND,"没有标签");
        }
        return responseMsgVO.buildOKWithData(list);
    }

    @GetMapping(value = "tag-page/pageRows:{pageRows}/pageNum:{pageNum}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO queryTagByPage(@PathVariable(name = "pageRows")long pageRows,@PathVariable(name = "pageNum")long pageNum){
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        PageInfo pageInfo =blogService.queryTagByPage(pageRows,pageNum);
        if (Objects.isNull(pageInfo.getCompleteData())){
            return responseMsgVO.buildWithMsgAndStatus(PosCodeEnum.NOT_FOUND,"没有标签");
        }
        return responseMsgVO.buildOKWithData(pageInfo);
    }

    @GetMapping(value = "title/{title}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO  queryByTitle(@PathVariable("title")String title){
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        List<BlogVO> list = blogService.getBlogByTitle(title);
        if (Objects.isNull(list)){
            return responseMsgVO.buildWithMsgAndStatus(PosCodeEnum.NOT_FOUND,"未知的标题");
        }
        return responseMsgVO.buildOKWithData(list);
    }

    @GetMapping(value = "title-all",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO queryTitleAll(){
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        List list =blogService.queryTitleAll();
        if (Objects.isNull(list)){
            return responseMsgVO.buildWithMsgAndStatus(PosCodeEnum.NOT_FOUND,"没有标题");
        }
        return responseMsgVO.buildOKWithData(list);
    }


    @GetMapping(value = "title-page/pageRows:{pageRows}/pageNum:{pageNum}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO queryTitleByPage(@PathVariable("pageRows")long pageRows,@PathVariable(name = "pageNum")long pageNum){
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        PageInfo pageInfo =blogService.queryTitleByPage(pageRows,pageNum);
        if (Objects.isNull(pageInfo.getCompleteData())){
            return responseMsgVO.buildWithMsgAndStatus(PosCodeEnum.NOT_FOUND,"没有标题");
        }
        return responseMsgVO.buildOKWithData(pageInfo);
    }

    @GetMapping(value = "date/{date}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO  queryByDate(@PathVariable("date")String dateString) throws ParseException {
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date= null;
        date = format.parse(dateString);
        List<BlogVO> list = blogService.getBlogByDate(date);
        if (Objects.isNull(list)){
            return responseMsgVO.buildWithMsgAndStatus(PosCodeEnum.NOT_FOUND, "无效的日期");
        }
        return responseMsgVO.buildOKWithData(list);
    }

    @GetMapping(value = "date-all",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO queryDateAll(){
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        List list =blogService.queryDateAll();
        if (Objects.isNull(list)){
            return responseMsgVO.buildWithMsgAndStatus(PosCodeEnum.NOT_FOUND, "没有日期");
        }
        return  responseMsgVO.buildOKWithData(list);
    }

    @GetMapping(value = "date-page/pageRows:{pageRows}/pageNum:{pageNum}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO queryDateByPage(@PathVariable(name = "pageRows") long pageRows, @PathVariable(name = "pageNum") long pageNum) {
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        PageInfo pageInfo = blogService.queryDateByPage(pageRows, pageNum);
        if (Objects.isNull(pageInfo.getCompleteData())){
            return responseMsgVO.buildWithMsgAndStatus(PosCodeEnum.NOT_FOUND, "没有日期");
        }
        return responseMsgVO.buildOKWithData(pageInfo);
    }

    @GetMapping(value = "all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO queryAllBlog() {
        ResponseMsgVO<List<BlogVO>> responseMsgVO = new ResponseMsgVO();
        //List<BlogVO> list = blogService.queryAll();
        List<BlogVO> list = blogService.queryAllOfIdMap();
        return responseMsgVO.buildOKWithData(list);
    }

    @GetMapping(value = "page/pageRows:{pageRows}/pageNum:{pageNum}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public  ResponseMsgVO queryByPage(@PathVariable("pageRows")long pageRows,@PathVariable("pageNum")long pageNum){
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        PageInfo pageInfo= blogService.queryByPage(pageRows, pageNum);
        return responseMsgVO.buildOKWithData(pageInfo);
    }

    @DeleteMapping(value = "id/{id}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO blogDelete(@PathVariable("id") Long id) {
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        blogService.removeBlog(id,responseMsgVO);
        return responseMsgVO;
    }

    /**
     * 获得目前的最新博客ID
     * @return 最新博客ID
     */
    @GetMapping(value = "first-id", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO getCurrentId() {
        Long id =  blogService.getCurrentId();
        return new ResponseMsgVO().buildOKWithData(id);
    }


}
