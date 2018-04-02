package xyz.sanshan.common.exception.auth;

import xyz.sanshan.common.exception.CheckException;

public class ClientForbiddenException extends CheckException {

    public ClientForbiddenException(String message) {
        super(message);
    }
}
