package xyz.sanshan.auth.common.util.jwt;

import java.io.Serializable;

/**
 */
public class JWTInfo implements Serializable,IJWTInfo {
    private String username;
    private String userId;

    public JWTInfo(String username, String userId) {
        this.username = username;
        this.userId = userId;
    }


    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getId() {
        return null;
    }

}
