package com.sanshan.util.exception.dbrollback;

import com.sanshan.util.exception.CheckException;

public class DBRollbackDeleteException extends CheckException{

    public DBRollbackDeleteException() {

    }
    public DBRollbackDeleteException(String msg){
        super(msg);
    }

}
