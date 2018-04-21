package xyz.sanshan.gate.server.feign.fallback;

import org.springframework.stereotype.Component;
import xyz.sanshan.common.vo.PermissionInfo;
import xyz.sanshan.common.vo.ResponseMsgVO;
import xyz.sanshan.gate.server.feign.MainPermissionsService;

import java.util.LinkedList;
import java.util.List;

@Component
public class MainPermissionsServiceFallBack implements MainPermissionsService  {
    @Override
    public ResponseMsgVO<List<PermissionInfo>> getAllUserPermission() {
        ResponseMsgVO msgVO = new ResponseMsgVO();
        List<PermissionInfo> list = new LinkedList<>();
        return msgVO.buildOKWithData(list);
    }

    @Override
    public ResponseMsgVO<List<PermissionInfo>> getAllAdminPermission() {
        ResponseMsgVO msgVO = new ResponseMsgVO();
        List<PermissionInfo> list = new LinkedList<>();
        return msgVO.buildOKWithData(list);
    }
}
