package xyz.sanshan.auth.security.common.util.jwt;

import java.util.Date;

/**
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

    /**
     * 获取名称
     * @return
     */
    String getName();

    /**
     * 获取创建时间
     */
    Date getCreated();

}
