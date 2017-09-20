package com.sanshan.util.exception;

/**
 * 文件加载异常
 */
public class FileLoadException extends CheckException {
    public FileLoadException(){
        super();
    }

    public FileLoadException(String msg) {
        super(msg);
    }
}
