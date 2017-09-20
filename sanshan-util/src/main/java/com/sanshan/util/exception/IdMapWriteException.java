package com.sanshan.util.exception;

/**
 * 博客写入异常
 */
public class IdMapWriteException extends  CheckException{
    public IdMapWriteException(){
        super("IdMap写入过程出错");
    }

    public IdMapWriteException(String message) {
        super(message);
    }
}
