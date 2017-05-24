package com.sanshan.util.exception;

/**
 * Created by han on 2017/5/21.
 */
public class EditorTypeConventException extends Exception {

    public EditorTypeConventException() {
        super("在EditorType转换为类型时出错");
    }

    public EditorTypeConventException(String message) {
        super(message);
    }
}
