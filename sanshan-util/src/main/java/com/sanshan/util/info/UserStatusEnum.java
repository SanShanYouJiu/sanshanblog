package com.sanshan.util.info;

/**
 * Created by han on 2017/6/6.
 */
public enum  UserStatusEnum {

    NORMAL(1,"正常"),
    WAIT4EMAIL_CHECK(2,"待邮箱验证"),
    FREEZE(3,"冻结")
    ;

    public int value;
    public String msg;

    UserStatusEnum(int value, String msg) {
        this.value = value;
        this.msg = msg;
    }

}
