package xyz.sanshan.auth.security.client.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;
import xyz.sanshan.common.vo.ResponseMsgVO;

import java.util.List;

/**
 */
@FeignClient(value = "${auth.serviceId}")
public interface ServiceAuthFeign {
    @PostMapping(value = "/api/client/servicePubKey")
    public ResponseMsgVO<byte[]> getServicePublicKey(@RequestParam("clientId") String clientId, @RequestParam("secret") String secret);

    @PostMapping(value = "/api/client/token")
    public ResponseMsgVO<String> getAccessToken(@RequestParam("clientId") String clientId, @RequestParam("secret") String secret);

    @GetMapping(value = "/api/client/myClient")
    public ResponseMsgVO<List<String>> getAllowedClient(@RequestParam("serviceId") String serviceId, @RequestParam("secret") String secret);

    @PostMapping(value = "/api/client/userPubKey")
    public ResponseMsgVO<byte[]> getUserPublicKey(@RequestParam("clientId") String clientId, @RequestParam("secret") String secret);

}
