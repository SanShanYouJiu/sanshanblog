package xyz.sanshan.web.controller.vote;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import xyz.sanshan.service.vo.ResponseMsgVO;
import xyz.sanshan.service.vote.BlogVoteInfoService;
import xyz.sanshan.service.vote.UserVoteInfoService;
import xyz.sanshan.service.vote.VoteService;

/**
 * 投票相关
 */
@RestController
@RequestMapping("/blog/vote")
public class VoteController {

    @Autowired
    private VoteService voteService;

    @Autowired
    private UserVoteInfoService userVoteInfo;

    @Autowired
    private BlogVoteInfoService blogInfoService;


    @PostMapping(value = "blogId:{blogId}/favour",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO favourBlog(@RequestHeader("X-Real-IP")String ip, @PathVariable(value = "blogId")Long blogId){
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        voteService.anonymousVote(ip,blogId,true,responseMsgVO);
        return responseMsgVO;
    }

    @PostMapping(value = "blogId:{blogId}/tread",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO treadBlog(@RequestHeader("X-Real-IP")String ip,@PathVariable(value = "blogId")Long blogId){
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        voteService.anonymousVote(ip,blogId,false,responseMsgVO);
        return  responseMsgVO;
    }

    @GetMapping(value = "blogId:{blogId}/info", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO blogInfo(@PathVariable(value = "blogId") Long blogId) {
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        blogInfoService.queryBlogInfo(blogId,responseMsgVO);
        return responseMsgVO;
    }

    @GetMapping(value = "ip:{ip}/ip-vote-info",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public  ResponseMsgVO ipVoteInfo(@PathVariable(value = "ip")String ip){
        ResponseMsgVO msgVO = new ResponseMsgVO();
        voteService.ipVoteInfo(ip, msgVO);
        return msgVO;
    }

    @GetMapping(value = "user:{username}/info", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO userVoteInfo(@PathVariable(value = "username") String username) {
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        userVoteInfo.getInfo(username,responseMsgVO);
        return responseMsgVO;
    }

}
