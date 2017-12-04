package com.sanshan.util.exception;

/**
 */
public class PropertiesConventException extends CheckException {

    public PropertiesConventException() {
        super("在Properties类型文件转换时出错");
    }

    public PropertiesConventException(String message) {
        super(message);
    }
}
