package xyz.sanshan.auth.security.common.util.jwt;

import java.io.Serializable;
import java.util.Date;

/**
 */
public class JWTInfo implements Serializable,IJWTInfo {
    private String username;
    private String userId;
    private Date created;

    public JWTInfo(String username, String userId,Date created) {
        this.username = username;
        this.userId = userId;
        this.created=created;
    }


    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getId() {
        return userId;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Date getCreated() {
        return this.created;
    }

}
