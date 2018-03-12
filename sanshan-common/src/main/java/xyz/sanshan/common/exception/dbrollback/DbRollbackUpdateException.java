package xyz.sanshan.common.exception.dbrollback;

import xyz.sanshan.common.exception.CheckException;

public class DbRollbackUpdateException extends CheckException {

    public DbRollbackUpdateException(){

    }
    public DbRollbackUpdateException(String message) {
        super(message);
    }
}
