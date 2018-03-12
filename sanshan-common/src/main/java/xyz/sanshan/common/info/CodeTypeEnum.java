package xyz.sanshan.common.info;

/**
 * 验证码类型
 */
public enum CodeTypeEnum {
    REGISTER(1, "注册"),
    CHANGE_PWD(2,"更改密码");

    private int value;
    private String msg;

    CodeTypeEnum(int value, String msg) {
        this.value = value;
        this.msg = msg;
    }

    public static CodeTypeEnum of(int type){
        for (CodeTypeEnum codeType : CodeTypeEnum.values()) {
            if (codeType.getValue() == type) {
                return codeType;
            }
        }
        return null;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
