package xyz.sanshan.auth.security.server.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import xyz.sanshan.auth.security.server.config.KeyConfiguration;
import xyz.sanshan.auth.security.server.service.AuthClientService;
import xyz.sanshan.auth.security.server.service.AuthService;
import xyz.sanshan.common.info.PosCodeEnum;
import xyz.sanshan.common.vo.ResponseMsgVO;

import java.util.ArrayList;
import java.util.List;

/**
 */
@Slf4j
@RestController
@RequestMapping("api/client")
public class ClientController {

    @Autowired
    private KeyConfiguration keyConfiguration;

    @Autowired
    private AuthClientService authClientService;

    @Autowired
    private AuthService authService;

    @PostMapping(value = "/token",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO<String> getAccessToken(@RequestParam("clientId") String clientId, @RequestParam("secret") String secret) throws Exception {
        ResponseMsgVO<String> msgVO = new ResponseMsgVO<String>();
        msgVO.buildOKWithData(authClientService.apply(clientId, secret));
        return msgVO;
    }


    @GetMapping(value = "/myClient",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO<List<String>> getAllowedClient(@RequestParam("serviceId")String serviceId, @RequestParam("secret")String secret) {
        ResponseMsgVO<List<String>>  msgVO = new ResponseMsgVO<List<String>> ();
        //TODO: 2018:4:5 授权的客户端列表
        msgVO.buildOKWithData(new ArrayList<>());
        return msgVO;
    }

    @PostMapping(value = "/user/token",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO<String> generateUserToken(@RequestParam("clientId") String clientId,@RequestParam("secret") String secret,@RequestParam("username") String username,@RequestParam("password") String password) throws Exception {
        ResponseMsgVO<String> msgVO = new ResponseMsgVO<String>();
        authClientService.validate(clientId, secret);
        //用户token生成
        String token = authService.login(username, password);
        //登录失败
        if (token==null){
            return msgVO.buildWithMsgAndStatus(PosCodeEnum.PARAM_ERROR,"用户名或密码错误");
        }
        msgVO.buildOKWithData(token);
        return msgVO;
    }

    @PostMapping(value = "/servicePubKey",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO<byte[]> getServicePublicKey(@RequestParam("clientId") String clientId, @RequestParam("secret") String secret) throws Exception {
        ResponseMsgVO<byte[]> msgVO = new ResponseMsgVO();
        //验证
        authClientService.validate(clientId, secret);
        msgVO.buildOKWithData(keyConfiguration.getServicePubKey());
        return  msgVO;
    }

    @PostMapping(value = "/userPubKey",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO<byte[]> getUserPublicKey(@RequestParam("clientId") String clientId, @RequestParam("secret") String secret) throws Exception {
        ResponseMsgVO<byte[]> msgVO = new ResponseMsgVO();
        authClientService.validate(clientId, secret);
        msgVO.buildOKWithData(keyConfiguration.getUserPubKey());
        return  msgVO;
    }


}
