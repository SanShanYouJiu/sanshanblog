package xyz.sanshan.search.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.sanshan.common.vo.PermissionInfo;
import xyz.sanshan.common.vo.ResponseMsgVO;
import xyz.sanshan.search.service.PermissionService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PermissonRpcController {

    @Autowired
    private PermissionService permissionService;

    @GetMapping(value = "/admin-permissions",produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public  ResponseMsgVO<List<PermissionInfo>> getAllAdminPermission(){
        ResponseMsgVO<List<PermissionInfo>> responseMsgVO = new ResponseMsgVO();
        responseMsgVO.buildOKWithData(permissionService.getAllAdminPermission());
        return responseMsgVO;
    }
}
