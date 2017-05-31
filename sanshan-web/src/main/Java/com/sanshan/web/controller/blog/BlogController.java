package com.sanshan.web.controller.blog;

import com.sanshan.service.BlogService;
import com.sanshan.service.vo.BlogVO;
import com.sanshan.service.vo.ResponseMsgVO;
import com.sanshan.util.BlogIdGenerate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RequestMapping("blog")
@RestController
public class BlogController {


    @Autowired
    BlogService blogService;

    @Autowired
    BlogIdGenerate blogIdGenerate;

    @RequestMapping("query-by-id")
    public ResponseMsgVO GetBlog(@RequestParam("id") Long id) throws Exception {
        ResponseMsgVO<BlogVO> responseMsgVO = new ResponseMsgVO<>();
        BlogVO blog = blogService.getBlog(id);
        return responseMsgVO.buildOKWithData(blog);
    }

    @RequestMapping("query-all")
    public ResponseMsgVO queryAllBlog() {
        ResponseMsgVO<List<BlogVO>> responseMsgVO = new ResponseMsgVO();
        List<BlogVO> list = blogService.queryAll();
        return responseMsgVO.buildOKWithData(list);
    }


    @RequestMapping("delete-by-id")
    public ResponseMsgVO blogState(@RequestParam("id") Long id) {
        //id去除
        ResponseMsgVO responseMsgVO= blogService.removeBlog(id);
        return responseMsgVO;
    }


}
