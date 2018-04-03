package xyz.sanshan.common.exception;

import xyz.sanshan.common.info.PosCodeEnum;

/**
 * 博客写入异常
 */
public class IdMapWriteException extends  CheckException{
    public IdMapWriteException(){
        super("IdMap写入过程出错", PosCodeEnum.INTER_ERROR.getStatus());
    }

    public IdMapWriteException(String message) {
        super(message,PosCodeEnum.INTER_ERROR.getStatus());
    }
}
