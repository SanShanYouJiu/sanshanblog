package com.sanshan.pojo.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document
@Data
@ToString
public class OAuth2UserDO {

    private String _id;

    /**
     *头像
     */
    private String avatar;

    private String username;

    private String email;

    private String blogLink;


    /**
     * 权限
     */
    private List<String> roles;

    private Integer status;

    private String url;

    private String OAuthType;

    /**
     * 是否锁定 1是 0否
     */
    private Integer enabled;

    /**
     * 登录失败次数
     */
    private Integer loginFail;

    /**
     * 锁定日期
     */
    private Date lockDate;


}
