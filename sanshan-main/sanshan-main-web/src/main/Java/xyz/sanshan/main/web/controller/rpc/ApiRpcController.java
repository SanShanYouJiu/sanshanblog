package xyz.sanshan.main.web.controller.rpc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import xyz.sanshan.common.info.PosCodeEnum;
import xyz.sanshan.common.vo.PermissionInfo;
import xyz.sanshan.common.vo.ResponseMsgVO;
import xyz.sanshan.main.api.vo.user.UserInfo;
import xyz.sanshan.main.service.auth.AuthService;
import xyz.sanshan.main.service.auth.PermissionService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiRpcController {

  @Autowired
  private PermissionService permissionService;

  @Autowired
  private AuthService authService;

    @GetMapping(value = "/user-permissions",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public  ResponseMsgVO<List<PermissionInfo>> getAllUserPermission(){
      ResponseMsgVO<List<PermissionInfo>> responseMsgVO = new ResponseMsgVO();
      responseMsgVO.buildOKWithData(permissionService.getAllUserPermission());
      return responseMsgVO;
    }

  @PostMapping(value = "/user/validate",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
 public ResponseMsgVO<UserInfo> validate(@RequestParam("username") String username, @RequestParam("password") String password){
    ResponseMsgVO responseMsgVO = new ResponseMsgVO();
    UserInfo userInfo = authService.validate(username, password);
    if (userInfo!=null){
      responseMsgVO.buildOKWithData(userInfo);
    }else {
      responseMsgVO.buildWithPosCode(PosCodeEnum.PARAM_ERROR);
    }
    return responseMsgVO;
  }

}
