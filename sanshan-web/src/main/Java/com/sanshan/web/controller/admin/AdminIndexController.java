package com.sanshan.web.controller.admin;

import com.sanshan.pojo.dto.UserDTO;
import com.sanshan.service.AdminIndexService;
import com.sanshan.service.vo.BlogVO;
import com.sanshan.service.vo.ResponseMsgVO;
import com.sanshan.util.info.PosCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/index")
public class AdminIndexController {

    @Autowired
    private AdminIndexService adminIndexService;


    @RequestMapping(value = "/blog/query-all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO queryAllBlog() {
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        List<BlogVO> list;
        list = adminIndexService.queryAllBlog();
        return responseMsgVO.buildOKWithData(list);
    }


    @RequestMapping(value = "/blog/query-ueditor-all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO queryUEditorAll() {
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        List<BlogVO> list;
        list = adminIndexService.queryUEditorBlogAll();
        return responseMsgVO.buildOKWithData(list);
    }


    @RequestMapping(value = "/blog/query-markdown-all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO queryMarkdownAll() {
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        List<BlogVO> list;
        list = adminIndexService.queryMarkdownBlogAll();
        return responseMsgVO.buildOKWithData(list);
    }


    @RequestMapping(value = "/get-user-info", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO currentUserInfo() {
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        UserDTO userDTO = adminIndexService.getUserInfo();
        return responseMsgVO.buildOKWithData(userDTO);
    }


    @RequestMapping(value = "/blog/update-by-id")
    public ResponseMsgVO updateBlog(@RequestParam(name = "id",required = true)Long id,
                                    @RequestParam(name = "title",required = false)String title,
                                    @RequestParam(name = "tag" ,required = false)String tag,
                                    @RequestParam(name = "content",required = false)String content){
        ResponseMsgVO msgVO = new ResponseMsgVO();
        adminIndexService.updateBlogById(id,title,tag,content,msgVO);
        return msgVO;
    }


    @RequestMapping(value = "/change-user-info", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO changeUserInfo(
            @RequestParam(name = "username", required = true) String username,
            @RequestParam(name = "avatar", required = false) String avatar,
            @RequestParam(name = "blogLink", required = false) String blogLink
             ) {
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        if (avatar!=null||blogLink!=null) {
            if (avatar != ""|| blogLink != "") {
                Map<String, String> mapList = new HashMap<>();
                mapList.put("avatar", avatar);
                mapList.put("blogLink", blogLink);
                if (adminIndexService.changeUserInfo(username, mapList))
                 return   responseMsgVO.buildOK();
            }
        }
        return responseMsgVO.buildWithMsgAndStatus(PosCodeEnum.PARAM_ERROR,"没有提供修改参数值");
    }

}
