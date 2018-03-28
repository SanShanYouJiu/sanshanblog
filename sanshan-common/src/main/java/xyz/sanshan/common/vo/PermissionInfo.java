package xyz.sanshan.common.vo;

import xyz.sanshan.common.info.HttpMethodEnum;

import java.io.Serializable;

/**
 */
public class PermissionInfo implements Serializable{
    private String uri;
    private HttpMethodEnum method;
    private String name;

    public PermissionInfo(String uri, HttpMethodEnum method, String name) {
        this.uri = uri;
        this.method = method;
        this.name = name;
    }

    public PermissionInfo() {
    }

    public HttpMethodEnum getMethod() {
        return method;
    }

    public void setMethod(HttpMethodEnum method) {
        this.method = method;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
