package xyz.sanshan.common.exception;

public class PropertyAccessException extends CheckException {

    public PropertyAccessException() {
        super("类型访问错误");
    }

    public PropertyAccessException(String message) {
        super(message);
    }
}
