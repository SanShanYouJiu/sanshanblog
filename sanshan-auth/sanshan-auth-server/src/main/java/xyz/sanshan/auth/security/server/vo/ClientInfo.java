package xyz.sanshan.auth.security.server.vo;


import xyz.sanshan.auth.security.common.util.jwt.IJWTInfo;

import java.util.Date;

/**
 */
public class ClientInfo implements IJWTInfo {
    private String clientId;
    private String name;
    private String id;
    private Date created;

    public ClientInfo(String clientId, String name, String id,Date created) {
        this.clientId = clientId;
        this.name = name;
        this.id = id;
        this.created = created;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String getUsername() {
        return clientId;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Date getCreated() {
        return created;
    }
}
