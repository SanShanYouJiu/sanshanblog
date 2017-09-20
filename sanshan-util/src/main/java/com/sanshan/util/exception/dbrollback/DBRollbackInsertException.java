package com.sanshan.util.exception.dbrollback;

import com.sanshan.util.exception.CheckException;

public class DBRollbackInsertException extends CheckException {

    public DBRollbackInsertException(){

    }
    public DBRollbackInsertException(String message) {
        super(message);
    }

}
