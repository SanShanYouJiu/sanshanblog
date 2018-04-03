package xyz.sanshan.common.exception.auth;

import xyz.sanshan.common.exception.CheckException;
import xyz.sanshan.common.info.PosCodeEnum;

public class ClientForbiddenException extends CheckException {

    public ClientForbiddenException(String message) {
        super(message, PosCodeEnum.CLIENT_FORBIDDEN_CODE.getStatus());
    }
}
