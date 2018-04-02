package xyz.sanshan.main.service.auth;

import org.springframework.stereotype.Service;
import xyz.sanshan.common.info.HttpMethodEnum;
import xyz.sanshan.common.vo.PermissionInfo;

import java.util.LinkedList;
import java.util.List;

@Service
public class PermissionService {

    /**
     * 需要授权访问的 只有用户才能访问
     * 默认情况下 其他的URL都是可以被访问的
     * @return
     */
    public List<PermissionInfo> getAllUserPermission(){
        List<PermissionInfo> result = new LinkedList<>();
        result.add(new PermissionInfo("/admin/index/**", HttpMethodEnum.ALL,""));
        result.add(new PermissionInfo("/user/change-pwd",HttpMethodEnum.ALL,""));
        result.add(new PermissionInfo("/user/email-check",HttpMethodEnum.ALL,""));
        result.add(new PermissionInfo("/auth/refresh-token",HttpMethodEnum.ALL,""));
        result.add(new PermissionInfo("/auth/login-status",HttpMethodEnum.ALL,""));
        result.add(new PermissionInfo("/user/register/check/token",HttpMethodEnum.ALL,""));
        result.add(new PermissionInfo("/markdown-editor/**",HttpMethodEnum.ALL,""));
        result.add(new PermissionInfo("/file/**",HttpMethodEnum.ALL,""));
        result.add(new PermissionInfo("/ueditor-editor/blog",HttpMethodEnum.ALL,""));
        result.add(new PermissionInfo("/ueditor-editor/blog/**",HttpMethodEnum.ALL,""));
        result.add(new PermissionInfo("/blog/id/**", HttpMethodEnum.DELETE, ""));
        result.add(new PermissionInfo("/api/user-permissions", HttpMethodEnum.ALL, ""));
        return result;
    }
}
