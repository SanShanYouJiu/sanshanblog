package com.sanshan.util.Setting;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 后台设置类
 * 目前还没有实现
 */
@Data
@NoArgsConstructor
public class Setting implements Serializable {

    private static final long serialVersionUID = -807062687563656085L;
    /**
     * 网站主域名
     */
    private String domain;
    /**
     * isEmailLogin : true
     * accountLockCount : 5
     * accountLockTime : 5000
     */
    /**
     * 是否采用邮件登录
     */
    private Boolean isEmailLogin;
    /**
     * 密码错误该次数后锁定账户
     */
    private Integer accountLockCount;
    /**
     * 锁定时间,单位分钟
     */
    private Integer accountLockTime;
    /**
     * cookies位置
     */
    private String cookiePath;
    /**
     * cookies作用域
     */
    private String cookieDomain;
    /**
     * 是否启用注册
     */
    private Boolean isRegisterEnabled;
    /**
     * 禁止注册的用户名
     */
    private String disabledUsernames;
    /**
     * 侧边栏标签数
     */
    private Integer aside_tags;
    /**
     * 侧边栏文章数
     */
    private Integer aside_articles;
    /**
     * isCommentEnabled : true
     * isCommentChecked : false
     */
    /**
     * 是否开启评论
     */
    private Boolean isCommentEnabled;
    /**
     * 是否开启评论审核
     */
    private Boolean isCommentChecked;
    /**
     * 通知数量
     */
    private Integer notify_count;


}
