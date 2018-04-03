package xyz.sanshan.common.exception;

import xyz.sanshan.common.info.PosCodeEnum;

public class NotFoundBlogException extends CheckException{

    public NotFoundBlogException(String message){
        super(message, PosCodeEnum.NOT_FOUND.getStatus());
    }


}
