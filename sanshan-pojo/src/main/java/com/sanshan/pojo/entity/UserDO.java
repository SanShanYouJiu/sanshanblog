package com.sanshan.pojo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "user")
@NoArgsConstructor
@Data
@ToString
public class UserDO extends BaseDO {
    /**
     * ID
     */
    private Long id;

    /**
     *头像
     */
    private String avatear;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 密码
     */
    private String passowrd;

    /**
     * 博客链接
     */
    private String blog;

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
    @Column(name = "is_lock")
    private Integer isLock;

    /**
     * 登录失败次数
     */
    private Integer loginfail;

    /**
     * 锁定日期
     */
    private Date lockdate;


    private Integer status;

    private String extend;

}
