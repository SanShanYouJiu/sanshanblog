package com.sanshan.web.controller.admin;

import com.sanshan.pojo.dto.UserDTO;
import com.sanshan.service.AdminIndexService;
import com.sanshan.service.vo.BlogVO;
import com.sanshan.service.vo.ResponseMsgVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/index")
@PreAuthorize("hasRole('USER')")
public class AdminIndexController {

    @Autowired
    private AdminIndexService adminIndexService;


    @RequestMapping(value = "/blog/query-all",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO queryAllBlog(){
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        List<BlogVO> list ;
        list=adminIndexService.queryAllBlog();
        return responseMsgVO.buildOKWithData(list);
    }


    @RequestMapping(value = "/blog/query-ueditor-all",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO queryUEditorAll(){
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        List<BlogVO> list ;
        list=adminIndexService.queryUEditorBlogAll();
        return responseMsgVO.buildOKWithData(list);
    }


    @RequestMapping(value = "/blog/query-markdown-all",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO queryMarkdownAll(){
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        List<BlogVO> list ;
        list=adminIndexService.queryMarkdownBlogAll();
        return responseMsgVO.buildOKWithData(list);
    }


    @RequestMapping(value = "/user-info",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO currentUserInfo(){
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
       UserDTO userDTO=adminIndexService.getUserInfo();
        return responseMsgVO.buildOKWithData(userDTO);
    }


}
