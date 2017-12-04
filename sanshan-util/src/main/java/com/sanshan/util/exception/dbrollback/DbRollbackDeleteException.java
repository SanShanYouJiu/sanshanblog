package com.sanshan.util.exception.dbrollback;

import com.sanshan.util.exception.CheckException;

public class DbRollbackDeleteException extends CheckException{

    public DbRollbackDeleteException() {

    }
    public DbRollbackDeleteException(String msg){
        super(msg);
    }

}
