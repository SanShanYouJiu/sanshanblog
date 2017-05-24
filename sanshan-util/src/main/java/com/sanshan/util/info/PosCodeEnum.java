package com.sanshan.util.info;

/**
 * 全局错误码配置
 * @author Niu Li
 */
public enum PosCodeEnum {

    OK(0,"SUCCESS"),USERNAME_EXIST(20001,"该账户已存在"),USERNAME_NOALLOW(20002,"用户名不合法"), CODE_ERROR(20003, "验证码错误"),
    NOOPEN_REGISTER(50001,"未开放注册"),PASSWORD_NOALLOW(20004,"密码不合法"),NICKNAME_NOALLOW(20005,"昵称不合法"),URL_ERROR(20006,"错误的链接"),
    ALREADY_REGISTER(20007,"未注册或已验证"),NO_REGISTER(20008,"未注册,请前往注册"),USER_LOCKED(20009,"账户被锁定"),
    USER_FREEZE(20010,"账户被冻结"),NO_LOGIN(20011,"用户未登录"),NO_PRIVILEGE(20012,"没有权限访问"),
    PARAM_ERROR(20013,"错误的参数值"), INTER_ERROR(20014, "内部错误"),
    ;


    private Integer status;
    private String msg;

    PosCodeEnum(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
