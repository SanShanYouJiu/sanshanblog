package xyz.sanshan.gate.server.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import xyz.sanshan.common.vo.PermissionInfo;
import xyz.sanshan.common.vo.ResponseMsgVO;

import java.util.List;

@FeignClient(name = "sanshan-main")
public interface MainPermissionsService {

    @GetMapping("/api/user-permissions")
    ResponseMsgVO<List<PermissionInfo>> getAllUserPermission();

    @GetMapping("/api/admin-permissions")
    ResponseMsgVO<List<PermissionInfo>> getAllAdminPermission();
}
