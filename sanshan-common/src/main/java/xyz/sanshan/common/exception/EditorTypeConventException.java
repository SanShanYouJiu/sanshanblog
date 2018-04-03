package xyz.sanshan.common.exception;

import xyz.sanshan.common.info.PosCodeEnum;

/**
 */
public class EditorTypeConventException extends CheckException {

    public EditorTypeConventException() {
        super("在EditorType转换为类型时出错", PosCodeEnum.INTER_ERROR.getStatus());
    }

    public EditorTypeConventException(String message) {
        super(message,PosCodeEnum.INTER_ERROR.getStatus());
    }
}
