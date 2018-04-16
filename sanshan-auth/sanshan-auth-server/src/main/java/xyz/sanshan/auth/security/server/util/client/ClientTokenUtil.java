package xyz.sanshan.auth.security.server.util.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import xyz.sanshan.auth.security.common.util.jwt.IJWTInfo;
import xyz.sanshan.auth.security.common.util.jwt.JWTHelper;
import xyz.sanshan.auth.security.server.config.KeyConfiguration;

@Configuration
@Slf4j
public class ClientTokenUtil {

    @Value("${client.expire}")
    private int expire;

    @Autowired
    private KeyConfiguration keyConfiguration;

    public String generateToken(IJWTInfo jwtInfo) throws Exception {
        return JWTHelper.generateToken(jwtInfo, keyConfiguration.getServicePriKey(), expire);
    }

    public IJWTInfo getInfoFromToken(String token) throws Exception {
        return JWTHelper.getInfoFromToken(token, keyConfiguration.getServicePubKey());
    }
}
