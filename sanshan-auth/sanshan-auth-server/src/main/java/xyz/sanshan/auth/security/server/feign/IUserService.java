package xyz.sanshan.auth.security.server.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import xyz.sanshan.common.vo.ResponseMsgVO;
import xyz.sanshan.main.api.vo.user.UserInfo;

@FeignClient(name = "sanshan-main")
public interface IUserService {

    @PostMapping(value = "api/user/validate",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseMsgVO<UserInfo> validate(@RequestParam("username") String username, @RequestParam("password") String password);

}
