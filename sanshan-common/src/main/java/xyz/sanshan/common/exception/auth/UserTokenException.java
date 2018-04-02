package xyz.sanshan.common.exception.auth;


import xyz.sanshan.common.exception.CheckException;

/**
 */
public class UserTokenException extends CheckException {
    public UserTokenException(String message) {
        super(message);
    }
}
