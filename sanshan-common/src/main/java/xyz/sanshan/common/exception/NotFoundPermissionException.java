package xyz.sanshan.common.exception;

public class NotFoundPermissionException extends CheckException {
    public NotFoundPermissionException() {
        super("没有获得授权许可的异常");
    }

    public NotFoundPermissionException(String message) {
        super(message);
    }

}