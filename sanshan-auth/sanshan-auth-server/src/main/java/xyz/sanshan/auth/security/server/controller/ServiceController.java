package xyz.sanshan.auth.security.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import xyz.sanshan.auth.security.server.service.AuthServiceOperationService;
import xyz.sanshan.common.vo.ResponseMsgVO;

import java.util.List;

@RestController
@RequestMapping("/service")
public class ServiceController {

    @Autowired
    private AuthServiceOperationService authServiceOperationService;

    @PutMapping(value = "/{id}/client", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO modifyUsers(@PathVariable("id") int id, List<String> clients) {
        ResponseMsgVO msgVO = new ResponseMsgVO();
        authServiceOperationService.modifyClientServices(id, clients);
        return msgVO.buildOK();
    }

    @GetMapping(value = "/{id}/client", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO getUsers(@PathVariable("id") int id) {
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        responseMsgVO.buildOKWithData(authServiceOperationService.getClientServices(id));
        return responseMsgVO;
    }

}
