package xyz.sanshan.common.exception.dbrollback;

import xyz.sanshan.common.exception.CheckException;

public class DbRollbackInsertException extends CheckException {

    public DbRollbackInsertException(){

    }
    public DbRollbackInsertException(String message) {
        super(message);
    }

}
