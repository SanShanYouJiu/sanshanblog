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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PermissionInfo)) return false;

        PermissionInfo that = (PermissionInfo) o;

        if (getUri() != null ? !getUri().equals(that.getUri()) : that.getUri() != null) return false;
        if (getMethod() != that.getMethod()) return false;
        return getName() != null ? getName().equals(that.getName()) : that.getName() == null;
    }

    @Override
    public int hashCode() {
        int result = getUri() != null ? getUri().hashCode() : 0;
        result = 31 * result + (getMethod() != null ? getMethod().hashCode() : 0);
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        return result;
    }
}
