package xyz.sanshan.common.exception.auth;


import xyz.sanshan.common.exception.CheckException;
import xyz.sanshan.common.info.PosCodeEnum;

/**
 */
public class ClientInvalidException extends CheckException {
    public ClientInvalidException(String message) {
        super(message, PosCodeEnum.CLIENT_INVALID_CODE.getStatus());
    }

    }
