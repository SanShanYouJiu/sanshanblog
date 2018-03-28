package xyz.sanshan.auth.common.util.jwt;

/**
 * Created by ace on 2017/9/10.
 */
public interface IJWTInfo {
    /**
     * 获取用户名
     * @return
     */
    String getUsername();

    /**
     * 获取用户ID
     * @return
     */
    String getId();

}
