package com.sanshan.pojo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Data
@ToString
@Document
public class UserDO extends BaseDO {

    private static final long serialVersionUID = -499938264759024375L;

    /**
     * 利用Mongo中默认的ObjectId
     */
    private String _id;

    /**
     *头像
     */
    private String avatar;

    /**
     * 昵称
     */
    @Indexed(unique=true, direction= IndexDirection.DESCENDING, dropDups=true)
    private String username;

    /**
     * 电子邮箱
     */
    @Indexed(unique = true,direction = IndexDirection.DESCENDING,dropDups = true)
    private String email;

    /**
     * 密码
     */
    private String password;

    /**
     * 博客链接
     */
    private String blogLink;

    /**
     * 登录IP
     */
    private String ip;

    /**
     * 所得荣誉
     */
    private String honor;


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


    private Integer status;

    private String extend;

    /**
     * 权限
     */
    private List<String> roles;

    /**
     * 最后修改密码的时间
     */
    private Date lastPasswordResetDate;
}
