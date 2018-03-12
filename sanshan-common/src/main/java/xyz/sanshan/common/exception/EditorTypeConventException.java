package xyz.sanshan.common.exception;

/**
 */
public class EditorTypeConventException extends CheckException {

    public EditorTypeConventException() {
        super("在EditorType转换为类型时出错");
    }

    public EditorTypeConventException(String message) {
        super(message);
    }
}
