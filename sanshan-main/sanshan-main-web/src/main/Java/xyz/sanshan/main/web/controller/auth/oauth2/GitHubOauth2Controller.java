package xyz.sanshan.main.web.controller.auth.oauth2;

import xyz.sanshan.main.service.auth.oauth2.GithubOAuth2Service;
import xyz.sanshan.common.vo.ResponseMsgVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("github")
@Slf4j
public class GitHubOauth2Controller {

    @Autowired
    private GithubOAuth2Service githubOAuth2Service;

    @GetMapping("/login")
    public ResponseEntity login()  {
        MultiValueMap<String, String> headers =new LinkedMultiValueMap();
        headers.add("Location",githubOAuth2Service.getAuthorizationUrl());
        return new ResponseEntity(headers,HttpStatus.MOVED_PERMANENTLY);
    }

    @GetMapping(value = "/oauth2/callback" )
    public ResponseMsgVO callback(@RequestParam(value = "code", required = true) String code,
                           HttpServletRequest request){
        githubOAuth2Service.callback(code);
        return new ResponseMsgVO().buildOK();
    }


}
