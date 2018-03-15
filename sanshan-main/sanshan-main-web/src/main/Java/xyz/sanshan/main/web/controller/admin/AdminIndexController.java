package xyz.sanshan.main.web.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import xyz.sanshan.common.info.PosCodeEnum;
import xyz.sanshan.main.pojo.dto.UserDTO;
import xyz.sanshan.main.service.AdminIndexService;
import xyz.sanshan.main.service.vo.BlogVO;
import xyz.sanshan.main.service.vo.ResponseMsgVO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/index")
public class AdminIndexController {

    @Autowired
    private AdminIndexService adminIndexService;


    @GetMapping(value = "/blog/all",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO queryAllBlog() {
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        List<BlogVO> list;
        list = adminIndexService.queryAllBlog();
        return responseMsgVO.buildOKWithData(list);
    }


    @GetMapping(value = "/blog/ueditor-all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO queryUEditorAll() {
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        List<BlogVO> list;
        list = adminIndexService.queryUEditorBlogAll();
        return responseMsgVO.buildOKWithData(list);
    }


    @GetMapping(value = "/blog/markdown-all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO queryMarkdownAll() {
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        List<BlogVO> list;
        list = adminIndexService.queryMarkdownBlogAll();
        return responseMsgVO.buildOKWithData(list);
    }


    @GetMapping(value = "/user-info", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO currentUserInfo() {
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        UserDTO userDTO = adminIndexService.getUserInfo();
        return responseMsgVO.buildOKWithData(userDTO);
    }


    @PostMapping(value = "/blog")
    public ResponseMsgVO updateBlog(@RequestParam(name = "id",required = true)Long id,
                                    @RequestParam(name = "title",required = false)String title,
                                    @RequestParam(name = "tag" ,required = false)String tag,
                                    @RequestParam(name = "content",required = false)String content){
        ResponseMsgVO msgVO = new ResponseMsgVO();
        adminIndexService.updateBlogById(id,title,tag,content,msgVO);
        return msgVO;
    }


    @PostMapping(value = "/user-info", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO changeUserInfo(
            @RequestParam(name = "username", required = true) String username,
            @RequestParam(name = "avatar", required = false) String avatar,
            @RequestParam(name = "blogLink", required = false) String blogLink
             ) {
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        if (avatar!=null||blogLink!=null) {
            if (avatar != ""|| blogLink != "") {
                Map<String, String> mapList = new HashMap<>(5);
                mapList.put("avatar", avatar);
                mapList.put("blogLink", blogLink);
                if (adminIndexService.changeUserInfo(username, mapList)){
                    return   responseMsgVO.buildOK();
                }
            }
        }
        return responseMsgVO.buildWithMsgAndStatus(PosCodeEnum.PARAM_ERROR,"没有提供修改参数值");
    }

}
