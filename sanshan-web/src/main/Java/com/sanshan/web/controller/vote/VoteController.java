package com.sanshan.web.controller.vote;

import com.sanshan.service.VoteService;
import com.sanshan.service.vo.ResponseMsgVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 投票相关
 */
@RestController
@RequestMapping("/blog/vote")
public class VoteController {

    @Autowired
    private VoteService voteService;


    @RequestMapping(value = "/favour",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO favourBlog(@RequestHeader("X-Real-IP")String ip, @RequestParam(value = "blogId")Long blogId){
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        voteService.anonymousVote(ip,blogId,true,responseMsgVO);
        return responseMsgVO;
    }

    @RequestMapping(value = "/tread",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO treadBlog(@RequestHeader("X-Real-IP")String ip,@RequestParam(value = "blogId")Long blogId){
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        voteService.anonymousVote(ip,blogId,false,responseMsgVO);
        return  responseMsgVO;
    }

    @RequestMapping(value = "/blog-info", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO blogInfo(@RequestParam(value = "blogId") Long blogId) {
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        voteService.queryBlogInfo(blogId,responseMsgVO);
        return responseMsgVO;
    }

}
