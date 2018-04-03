package xyz.sanshan.common.exception.auth;


import xyz.sanshan.common.exception.CheckException;
import xyz.sanshan.common.info.PosCodeEnum;

/**
 */
public class UserTokenException extends CheckException {
    public UserTokenException(String message) {
        super(message, PosCodeEnum.USER_INVALID_CODE.getStatus());
    }
}
