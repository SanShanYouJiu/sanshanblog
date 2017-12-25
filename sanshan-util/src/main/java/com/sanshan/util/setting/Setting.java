package com.sanshan.util.setting;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Setting)) return false;
        Setting setting = (Setting) o;
        if (getDomain() != null ? !getDomain().equals(setting.getDomain()) : setting.getDomain() != null) return false;
        if (getIsEmailLogin() != null ? !getIsEmailLogin().equals(setting.getIsEmailLogin()) : setting.getIsEmailLogin() != null)
            return false;
        if (getAccountLockCount() != null ? !getAccountLockCount().equals(setting.getAccountLockCount()) : setting.getAccountLockCount() != null)
            return false;
        if (getAccountLockTime() != null ? !getAccountLockTime().equals(setting.getAccountLockTime()) : setting.getAccountLockTime() != null)
            return false;
        if (getCookiePath() != null ? !getCookiePath().equals(setting.getCookiePath()) : setting.getCookiePath() != null)
            return false;
        if (getCookieDomain() != null ? !getCookieDomain().equals(setting.getCookieDomain()) : setting.getCookieDomain() != null)
            return false;
        if (getIsRegisterEnabled() != null ? !getIsRegisterEnabled().equals(setting.getIsRegisterEnabled()) : setting.getIsRegisterEnabled() != null)
            return false;
        if (getDisabledUsernames() != null ? !getDisabledUsernames().equals(setting.getDisabledUsernames()) : setting.getDisabledUsernames() != null)
            return false;
        if (getIsCommentEnabled() != null ? !getIsCommentEnabled().equals(setting.getIsCommentEnabled()) : setting.getIsCommentEnabled() != null)
            return false;
        return getIsCommentChecked() != null ? getIsCommentChecked().equals(setting.getIsCommentChecked()) : setting.getIsCommentChecked() == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getDomain() != null ? getDomain().hashCode() : 0);
        result = 31 * result + (getIsEmailLogin() != null ? getIsEmailLogin().hashCode() : 0);
        result = 31 * result + (getAccountLockCount() != null ? getAccountLockCount().hashCode() : 0);
        result = 31 * result + (getAccountLockTime() != null ? getAccountLockTime().hashCode() : 0);
        result = 31 * result + (getCookiePath() != null ? getCookiePath().hashCode() : 0);
        result = 31 * result + (getCookieDomain() != null ? getCookieDomain().hashCode() : 0);
        result = 31 * result + (getIsRegisterEnabled() != null ? getIsRegisterEnabled().hashCode() : 0);
        result = 31 * result + (getDisabledUsernames() != null ? getDisabledUsernames().hashCode() : 0);
        result = 31 * result + (getIsCommentEnabled() != null ? getIsCommentEnabled().hashCode() : 0);
        result = 31 * result + (getIsCommentChecked() != null ? getIsCommentChecked().hashCode() : 0);
        return result;
    }
}
