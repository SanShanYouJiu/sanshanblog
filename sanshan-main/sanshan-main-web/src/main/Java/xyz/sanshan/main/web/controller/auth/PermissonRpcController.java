package xyz.sanshan.main.web.controller.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.sanshan.common.vo.ResponseMsgVO;
import xyz.sanshan.main.service.auth.PermissionService;

@RestController
@RequestMapping("/api")
public class PermissonRpcController {

  @Autowired
  private PermissionService permissionService;

    @GetMapping(value = "/user-permissions")
    public  ResponseMsgVO getAllUserPermission(){
      ResponseMsgVO responseMsgVO = new ResponseMsgVO();
      responseMsgVO.buildOKWithData(permissionService.getAllUserPermission());
      return responseMsgVO;
    }

}
