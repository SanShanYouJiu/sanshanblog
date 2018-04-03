package xyz.sanshan.common.exception;

import xyz.sanshan.common.info.PosCodeEnum;

public class NotFoundPermissionException extends CheckException {
    public NotFoundPermissionException() {
        super("没有获得授权许可的异常", PosCodeEnum.NO_PRIVILEGE.getStatus());
    }

    public NotFoundPermissionException(String message,int status) {
        super(message,status);
    }

}