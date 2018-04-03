package xyz.sanshan.common.exception;

import xyz.sanshan.common.info.PosCodeEnum;

/**
 */
public class PropertiesConventException extends CheckException {

    public PropertiesConventException() {
        super("在Properties类型文件转换时出错",PosCodeEnum.INTER_ERROR.getStatus());
    }

    public PropertiesConventException(String message) {
        super(message, PosCodeEnum.INTER_ERROR.getStatus());
    }
}
