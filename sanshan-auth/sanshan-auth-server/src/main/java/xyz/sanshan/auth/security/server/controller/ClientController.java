package xyz.sanshan.auth.security.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import xyz.sanshan.auth.security.server.config.KeyConfiguration;
import xyz.sanshan.common.vo.ResponseMsgVO;

/**
 */
@RestController
@RequestMapping("api/client")
public class ClientController {

    @Autowired
    private KeyConfiguration keyConfiguration;


    @GetMapping(value = "/myClient",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO getAllowedClient(String serviceId, String secret) {
        ResponseMsgVO msgVO = new ResponseMsgVO();
        //TODO: 2018:4:5 授权的客户端列表
        msgVO.buildOK();
        return msgVO;
    }


    @PostMapping(value = "/servicePubKey",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO<byte[]> getServicePublicKey(@RequestParam("clientId") String clientId, @RequestParam("secret") String secret) throws Exception {
        ResponseMsgVO<byte[]> msgVO = new ResponseMsgVO();
        //TODO：2018:4:4 验证clientId
        msgVO.buildOKWithData(keyConfiguration.getServicePubKey());
        return  msgVO;
    }

    @PostMapping(value = "/userPubKey",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO<byte[]> getUserPublicKey(@RequestParam("clientId") String clientId, @RequestParam("secret") String secret) throws Exception {
        ResponseMsgVO<byte[]> msgVO = new ResponseMsgVO();
        msgVO.buildOKWithData(keyConfiguration.getUserPubKey());
        return  msgVO;
    }


}
