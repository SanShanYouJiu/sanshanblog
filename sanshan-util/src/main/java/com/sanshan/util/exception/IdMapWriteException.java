package com.sanshan.util.exception;

/**
 * Created by han on 2017/5/16.
 */
public class IdMapWriteException extends  Exception{
    public IdMapWriteException(){
        super("IdMap写入过程出错");
    }

    public IdMapWriteException(String message) {
        super(message);
    }
}
