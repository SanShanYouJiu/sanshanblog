package xyz.sanshan.main.web.controller.recommend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.sanshan.common.vo.ResponseMsgVO;
import xyz.sanshan.main.service.task.RecommendTask;

/**
 */
@RestController
@RequestMapping("recommend")
public class RecommendController {

    @Autowired
    private RecommendTask recommendService;

    @GetMapping(value = "/users",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO recommendUsers() {
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        recommendService.recommendUsers(responseMsgVO);
        return responseMsgVO;
    }

    @GetMapping(value = "/blogs",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO recommendBlogs(){
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        recommendService.recommendBlogs(responseMsgVO);
        return responseMsgVO;
    }

}
