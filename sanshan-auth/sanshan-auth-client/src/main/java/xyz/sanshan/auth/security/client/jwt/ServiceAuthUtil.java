package xyz.sanshan.auth.security.client.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import xyz.sanshan.auth.security.client.config.ServiceAuthConfig;
import xyz.sanshan.auth.security.client.feign.ServiceAuthFeign;
import xyz.sanshan.auth.security.common.event.AuthRemoteEvent;
import xyz.sanshan.auth.security.common.util.jwt.IJWTInfo;
import xyz.sanshan.auth.security.common.util.jwt.JWTHelper;
import xyz.sanshan.common.exception.auth.ClientTokenException;
import xyz.sanshan.common.vo.ResponseMsgVO;

import java.util.List;

/**
 */
@Configuration
@Slf4j
@EnableScheduling
public class ServiceAuthUtil  implements ApplicationListener<AuthRemoteEvent> {
    @Autowired
    private ServiceAuthConfig serviceAuthConfig;
    @Autowired
    private ServiceAuthFeign serviceAuthFeign;
    private List<String> allowedClient;
    private String clientToken;

    public IJWTInfo getInfoFromToken(String token) throws Exception {
        try {
            return JWTHelper.getInfoFromToken(token, serviceAuthConfig.getPubKeyByte());
        } catch (ExpiredJwtException ex) {
            throw new ClientTokenException("Client token expired!");
        } catch (SignatureException ex) {
            throw new ClientTokenException("Client token signature error!");
        } catch (IllegalArgumentException ex) {
            throw new ClientTokenException("Client token is null or empty!");
        }
    }

    @Override
    public void onApplicationEvent(AuthRemoteEvent authRemoteEvent) {
        this.allowedClient = authRemoteEvent.getAllowedClient();
    }

    @Scheduled(cron = "0/30 * * * * ?")
    public void refreshAllowedClient() {
        log.debug("refresh allowedClient.....");
        ResponseMsgVO<List<String>> resp = serviceAuthFeign.getAllowedClient(serviceAuthConfig.getClientId(), serviceAuthConfig.getClientSecret());
        if (resp.getStatus() == 0) {
            this.allowedClient = resp.getData();
        }
    }

    @Scheduled(cron = "0 0/10 * * * ?")
    public void refreshClientToken() {
        log.debug("refresh client token.....");
        ResponseMsgVO<String> resp = serviceAuthFeign.getAccessToken(serviceAuthConfig.getClientId(), serviceAuthConfig.getClientSecret());
        if (resp.getStatus() == 0) {
            this.clientToken = resp.getData();
        }
    }


    public String getClientToken() {
        if (this.clientToken == null) {
            this.refreshClientToken();
        }
        return clientToken;
    }

    public List<String> getAllowedClient() {
        if (this.allowedClient == null) {
            this.refreshAllowedClient();
        }
        return allowedClient;
    }

}