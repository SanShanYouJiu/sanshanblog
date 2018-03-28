package xyz.sanshan.search.service;

import org.springframework.stereotype.Service;
import xyz.sanshan.common.info.HttpMethodEnum;
import xyz.sanshan.common.vo.PermissionInfo;

import java.util.LinkedList;
import java.util.List;

@Service
public class PermissionService {

    /**
     * 需要授权访问的 只有模块之间才能访问
     * 默认情况下 其他的URL都是可以被访问的
     * @return
     */
    public List<PermissionInfo> getAllAdminPermission(){
        List<PermissionInfo> result = new LinkedList<>();
        result.add(new PermissionInfo("/user-info", HttpMethodEnum.POST,""));
        result.add(new PermissionInfo("/markdown-info", HttpMethodEnum.POST,""));
        result.add(new PermissionInfo("/ueditor-info", HttpMethodEnum.POST,""));

        result.add(new PermissionInfo("/user-info/**", HttpMethodEnum.DELETE,""));
        result.add(new PermissionInfo("/ueditor-info/**", HttpMethodEnum.DELETE,""));
        result.add(new PermissionInfo("/markdown-info/**", HttpMethodEnum.DELETE,""));
        return result;
    }
}
