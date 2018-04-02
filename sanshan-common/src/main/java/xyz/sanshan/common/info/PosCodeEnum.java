package xyz.sanshan.common.info;

/**
 * 全局错误码配置
 */
public enum PosCodeEnum {

    OK(0,"SUCCESS"),NOT_FOUND(404,"不存在"), INTER_ERROR(500, "内部错误"),
     USER_INVALID_CODE(40010,"用户token异常"),CLIENT_INVALID_CODE(40301,"客户端token异常"),CLIENT_FORBIDDEN_CODE(40331,"客户端禁止异常")

    ,Email_EXIST(20001,"该邮箱已存在"),USERNAME_NOALLOW(20002,"用户名不合法"),EMAIL_NOALLOW(20003,"邮箱名不合法"),CODE_ERROR(20004, "验证码错误"), URL_ERROR(20005,"错误的链接"),
    NO_REGISTER(21000,"未注册,请前往注册"),USER_LOCKED(21001,"账户被锁定"),
    USER_FREEZE(21002,"账户被冻结")

    ,NO_PRIVILEGE(50401,"没有权限访问"),
    PARAM_ERROR(50417,"错误的参数值"), FREQUENTLY_REQUEST(50408,"请求太频繁"),UNKONW_EXCEPTION(50404,"未知错误")
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
