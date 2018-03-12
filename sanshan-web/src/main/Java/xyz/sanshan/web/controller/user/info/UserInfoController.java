package xyz.sanshan.web.controller.user.info;

import xyz.sanshan.service.user.info.UserInfoService;
import xyz.sanshan.service.vo.ResponseMsgVO;
import xyz.sanshan.common.info.PosCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

/**
 */
@RestController
@RequestMapping("user-info")
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    @GetMapping(value = "{username}/basic",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO getUserInfo(@PathVariable(name = "username") String username) {
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        userInfoService.getUserInfo(username, responseMsgVO);
        return responseMsgVO;
    }

    @GetMapping(value = "{username}/blogs",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO getUserBlogs(@PathVariable(name = "username") String username) {
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        List list = userInfoService.getUserBlogs(username);
        if (Objects.isNull(list)) {
            return responseMsgVO.buildWithMsgAndStatus(PosCodeEnum.NOT_FOUND, "数据不存在");
        }
        return responseMsgVO.buildOKWithData(list);
    }

}
