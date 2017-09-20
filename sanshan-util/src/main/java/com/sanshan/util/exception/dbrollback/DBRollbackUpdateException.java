package com.sanshan.util.exception.dbrollback;

import com.sanshan.util.exception.CheckException;

public class DBRollbackUpdateException extends CheckException {

    public DBRollbackUpdateException(){

    }
    public DBRollbackUpdateException(String message) {
        super(message);
    }
}
