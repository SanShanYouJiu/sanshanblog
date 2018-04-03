package xyz.sanshan.common.exception;

import xyz.sanshan.common.info.PosCodeEnum;

public class PropertyAccessException extends CheckException {

    public PropertyAccessException() {
        super("类型访问错误", PosCodeEnum.INTER_ERROR.getStatus());
    }

    public PropertyAccessException(String message) {
        super(message,PosCodeEnum.INTER_ERROR.getStatus());
    }
}
