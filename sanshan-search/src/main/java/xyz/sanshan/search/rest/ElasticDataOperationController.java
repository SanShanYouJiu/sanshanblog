package xyz.sanshan.search.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.sanshan.common.vo.ResponseMsgVO;
import xyz.sanshan.search.pojo.DTO.MarkDownBlogDTO;
import xyz.sanshan.search.pojo.DTO.UeditorBlogDTO;
import xyz.sanshan.search.pojo.DTO.UserDTO;
import xyz.sanshan.search.service.ElasticDataOperationService;

@RestController
public class ElasticDataOperationController {

    @Autowired
    private ElasticDataOperationService operationService;


    @PostMapping("/user-info")
    public ResponseMsgVO userAdd(@RequestBody UserDTO userDTO){
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        operationService.userAdd(userDTO);
        return responseMsgVO.buildOK();
    }

    @PostMapping("/markdown-info")
    public ResponseMsgVO markdownBlogAdd(@RequestBody MarkDownBlogDTO markDownBlogDTO){
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        operationService.markdownBlogAdd(markDownBlogDTO);
        return responseMsgVO.buildOK();

    }

    @PostMapping("/ueditor-info")
    public ResponseMsgVO ueditorBlogAdd(@RequestBody UeditorBlogDTO ueditorBlogDTO){
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        operationService.ueditorBlogAdd(ueditorBlogDTO);
        return responseMsgVO.buildOK();
    }

    @DeleteMapping("/user-info/{id}")
    public ResponseMsgVO userDelete(@PathVariable(name = "id")String id){
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        operationService.userDelete(id);
        return responseMsgVO.buildOK();
    }

    @DeleteMapping("/markdown-info/{id}")
    public ResponseMsgVO markdownBlogAdd(@PathVariable(name = "id")Long id){
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        operationService.markdownDelete(id);
        return responseMsgVO.buildOK();
    }

    @DeleteMapping("/ueditor-info/{id}")
    public ResponseMsgVO ueditorBlogAdd(@PathVariable(name = "id")String id){
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        operationService.userDelete(id);
        return responseMsgVO.buildOK();
    }

}
