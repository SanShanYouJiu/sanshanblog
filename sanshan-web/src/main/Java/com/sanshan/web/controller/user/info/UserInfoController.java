package com.sanshan.web.controller.user.info;

import com.sanshan.service.user.info.UserInfoService;
import com.sanshan.service.vo.ResponseMsgVO;
import com.sanshan.util.info.PosCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @RequestMapping(value = "basic",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO getUserInfo(@RequestParam(name = "username") String username) {
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        userInfoService.getUserInfo(username, responseMsgVO);
        return responseMsgVO;
    }

    @RequestMapping(value = "blogs",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO getUserBlogs(@RequestParam(name = "username") String username) {
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        List list = userInfoService.getUserBlogs(username);
        if (Objects.isNull(list)) {
            return responseMsgVO.buildWithMsgAndStatus(PosCodeEnum.NOT_FOUND, "数据不存在");
        }
        return responseMsgVO.buildOKWithData(list);
    }

}
