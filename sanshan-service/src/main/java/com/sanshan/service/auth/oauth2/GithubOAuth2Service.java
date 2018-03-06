package com.sanshan.service.auth.oauth2;

import com.github.scribejava.apis.GitHubApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.sanshan.service.SettingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class GithubOAuth2Service {
    @Autowired
    private SettingService settingService;
    @Autowired
    private RedisTemplate redisTemplate;

    private static final  String OAUTH2_USER_ACCESSTOKEN = "oauth2:user-accessToken:";

    private String secretState;
    private String callBackUrl;
    private String authorizationUrl;
    private OAuth20Service service;


    @Value("${github.oauth2.clientId}")
    private String githubClientId;
    @Value("${github.oauth2.clientSecret}")
    private String githubClientSecret;
    @Value("${github.oauth2.userInfoUri}")
    private String userInfoUri;
    @Value("${github.oauth2.accessTokenUri}")
    private String accessTokenUri;
    @Value("${github.oauth2.userAuthorizationUri}")
    private String userAuthorizationUri;

    @PostConstruct
    public void init() {
        secretState = "secret" + new Random().nextInt(999_999);
        //FIXME 这里的setting还没有数据 需要等到Bean容器全部初始化后才能使用
        //Setting setting = settingService.getSetting();
        //String domain = setting.getDomain();
        //callBackUrl = domain + "/api/github/oauth2/callback";
        final OAuth20Service service = new ServiceBuilder(githubClientId)
                .apiSecret(githubClientSecret)
                .state(secretState)
                .callback("http://localhost:8080/api/github/oauth2/callback")
                .build(GitHubApi.instance());
        this.service=service;
        authorizationUrl = service.getAuthorizationUrl();
    }

    /**
     * TODO 完成Github登录注册
     * @param code
     */
   public void callback(String code){
       log.info("code:"+code);
       try {
           final OAuth2AccessToken accessToken = service.getAccessToken(code);
           log.info(accessToken.toString());
           //redisTemplate.opsForValue().set(OAUTH2_USER_ACCESSTOKEN+"",accessToken);
           final OAuthRequest request = new OAuthRequest(Verb.GET, userInfoUri);
           service.signRequest(accessToken, request);
           final Response response = service.execute(request);
           log.info(response.getBody());
       } catch (IOException e) {
           e.printStackTrace();
       } catch (InterruptedException e) {
           e.printStackTrace();
       } catch (ExecutionException e) {
           e.printStackTrace();
       }
   }

    public String getSecretState() {
        return secretState;
    }

    public String getCallBackUrl() {
        return callBackUrl;
    }

    public String getAuthorizationUrl() {
        return authorizationUrl;
    }

}
