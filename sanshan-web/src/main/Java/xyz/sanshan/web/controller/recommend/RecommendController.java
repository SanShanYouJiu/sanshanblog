package xyz.sanshan.web.controller.recommend;

import xyz.sanshan.service.recommend.RecommendService;
import xyz.sanshan.service.vo.ResponseMsgVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 */
@RestController
@RequestMapping("recommend")
public class RecommendController {

    @Autowired
    private RecommendService recommendService;

    @GetMapping("/users")
    public ResponseMsgVO recommendUsers() {
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        recommendService.recommendUsers(responseMsgVO);
        return responseMsgVO;
    }

    @GetMapping("/blogs")
    public ResponseMsgVO recommendBlogs(){
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        recommendService.recommendBlogs(responseMsgVO);
        return responseMsgVO;
    }

}
