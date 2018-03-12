package xyz.sanshan.common.exception.dbrollback;

import xyz.sanshan.common.exception.CheckException;

public class DbRollbackDeleteException extends CheckException {

    public DbRollbackDeleteException() {

    }
    public DbRollbackDeleteException(String msg){
        super(msg);
    }

}
