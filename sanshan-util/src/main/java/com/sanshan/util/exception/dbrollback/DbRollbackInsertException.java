package com.sanshan.util.exception.dbrollback;

import com.sanshan.util.exception.CheckException;

public class DbRollbackInsertException extends CheckException {

    public DbRollbackInsertException(){

    }
    public DbRollbackInsertException(String message) {
        super(message);
    }

}
