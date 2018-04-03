package xyz.sanshan.auth.security.server.entity;


import xyz.sanshan.auth.security.common.util.jwt.IJWTInfo;

/**
 */
public class ClientInfo implements IJWTInfo {
    private String clientId;
    private String name;
    private String id;

    public ClientInfo(String clientId, String name, String id) {
        this.clientId = clientId;
        this.name = name;
        this.id = id;
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
}
