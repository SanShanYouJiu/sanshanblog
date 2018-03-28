package xyz.sanshan.auth.security.server.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import xyz.sanshan.common.vo.ResponseMsgVO;
import xyz.sanshan.main.api.vo.user.UserInfo;

@FeignClient(value = "sanshan-main" )
public interface IUserService {

    @PostMapping(value = "/api/user/validate")
    public ResponseMsgVO<UserInfo> validate(@RequestParam(name = "username") String username, @RequestParam(name = "password") String password);

}
