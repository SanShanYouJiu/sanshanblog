package xyz.sanshan.auth.security.server.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import xyz.sanshan.auth.security.server.config.KeyConfiguration;
import xyz.sanshan.auth.security.server.service.AuthClientService;
import xyz.sanshan.common.vo.ResponseMsgVO;

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

    @PostMapping(value = "/token",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO getAccessToken(String clientId, String secret) throws Exception {
        ResponseMsgVO msgVO = new ResponseMsgVO();
        msgVO.buildOKWithData(authClientService.apply(clientId, secret));
        return msgVO;
    }


    @GetMapping(value = "/myClient",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO<List<String>> getAllowedClient(String serviceId, String secret) {
        ResponseMsgVO msgVO = new ResponseMsgVO();
        //TODO: 2018:4:5 授权的客户端列表
        log.info("myclient:{},secret:{}",serviceId,secret);
        msgVO.buildOK();
        return msgVO;
    }

    @PostMapping(value = "/user/token",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO generateUserToken(String serviceId,String secret,String username,String password){
        ResponseMsgVO msgVO = new ResponseMsgVO();
        //TODO: 2018:4:15 用户token生成
        msgVO.buildOK();
        return msgVO;
    }

    @PostMapping(value = "/servicePubKey",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO<byte[]> getServicePublicKey(@RequestParam("clientId") String clientId, @RequestParam("secret") String secret) throws Exception {
        ResponseMsgVO<byte[]> msgVO = new ResponseMsgVO();
        //验证clientId
        log.info("myclient:{},secret:{}",clientId,secret);
        authClientService.validate(clientId, secret);
        msgVO.buildOKWithData(keyConfiguration.getServicePubKey());
        return  msgVO;
    }

    @PostMapping(value = "/userPubKey",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO<byte[]> getUserPublicKey(@RequestParam("clientId") String clientId, @RequestParam("secret") String secret) throws Exception {
        ResponseMsgVO<byte[]> msgVO = new ResponseMsgVO();
        log.info("userPubkey:{},secret:{}",clientId,secret);
        authClientService.validate(clientId, secret);
        msgVO.buildOKWithData(keyConfiguration.getUserPubKey());
        return  msgVO;
    }


}
