package xyz.sanshan.auth.security.client.exception;

/**
 */
public class JwtTokenExpiredException extends Exception {
    public JwtTokenExpiredException(String s) {
        super(s);
    }
}
