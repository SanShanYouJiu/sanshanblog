package xyz.sanshan.common.exception;

import xyz.sanshan.common.info.PosCodeEnum;

/**
 * 文件加载异常
 */
public class FileLoadException extends CheckException {
    public FileLoadException(){
        super("文件加载异常",PosCodeEnum.INTER_ERROR.getStatus());
    }

    public FileLoadException(String msg) {
        super(msg, PosCodeEnum.INTER_ERROR.getStatus());
    }
}
