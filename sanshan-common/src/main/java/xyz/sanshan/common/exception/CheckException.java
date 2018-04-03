package xyz.sanshan.common.exception;

/**
 * 受检查的异常
 */
public class CheckException  extends RuntimeException{
    private int status ;

    public CheckException() {
        super();
    }

    public CheckException(String message,int status) {
        super(message);
        this.status=status;
    }

    public CheckException(String message,int status, Throwable cause) {
        super(message, cause);
        this.status=status;
    }

    public CheckException(Throwable cause,int status) {
        super(cause);
        this.status=status;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
