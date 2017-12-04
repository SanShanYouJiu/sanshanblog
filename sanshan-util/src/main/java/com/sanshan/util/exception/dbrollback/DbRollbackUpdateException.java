package com.sanshan.util.exception.dbrollback;

import com.sanshan.util.exception.CheckException;

public class DbRollbackUpdateException extends CheckException {

    public DbRollbackUpdateException(){

    }
    public DbRollbackUpdateException(String message) {
        super(message);
    }
}
