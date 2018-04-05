package xyz.sanshan.auth.security.client.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import xyz.sanshan.common.vo.ResponseMsgVO;

/**
 */
@FeignClient(value = "${auth.serviceId}")
public interface ServiceAuthFeign {
    @RequestMapping(value = "/api/client/servicePubKey",method = RequestMethod.POST)
    public ResponseMsgVO<byte[]> getServicePublicKey(@RequestParam("clientId") String clientId, @RequestParam("secret") String secret);
    @RequestMapping(value = "/api/client/userPubKey",method = RequestMethod.POST)
    public ResponseMsgVO<byte[]> getUserPublicKey(@RequestParam("clientId") String clientId, @RequestParam("secret") String secret);

}
