package com.sanshan.pojo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

@Table(name = "user")
@NoArgsConstructor
@Data
@ToString
public class UserDO extends BaseDO {
    /**
     * ID
     */
    @Id
    private Long id;

    /**
     *头像
     */
    private String avatear;

    /**
     * 昵称
     */
    @Indexed(unique=true, direction= IndexDirection.DESCENDING, dropDups=true)
    private String username;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 密码
     */
    private String password;

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
    private Integer enabled;

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

    /**
     * 权限
     */
    private List<String> roles;

    /**
     * 最后修改密码的时间
     */
    private Date lastPasswordResetDate;
}
