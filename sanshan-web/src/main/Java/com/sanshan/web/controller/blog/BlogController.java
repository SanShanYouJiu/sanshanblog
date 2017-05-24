package com.sanshan.web.controller.blog;

import com.sanshan.service.BlogService;
import com.sanshan.service.vo.BlogVO;
import com.sanshan.util.BlogIdGenerate;
import com.sanshan.util.info.BlogOperationState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RequestMapping("blog")
@RestController
public class BlogController {


    @Autowired  BlogService blogService;

    @Autowired BlogIdGenerate blogIdGenerate;

    @RequestMapping("query-by-id")
    public BlogVO GetBlog(@RequestParam("id") Long id) throws Exception {
        BlogVO blog= blogService.getBlog(id);
        return blog;
    }

    @RequestMapping("query-all")
    public List<BlogVO> queryAllBlog(){
       return blogService.queryAll();
    }


    @RequestMapping("delete-by-id")
    public BlogOperationState blogState(@RequestParam("id")Long id){
        BlogOperationState sanShanBlogState= blogService.removeBlog(id);
        return sanShanBlogState;
    }


}
