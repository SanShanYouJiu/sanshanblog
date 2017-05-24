package com.sanshan.util.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND,reason = "没有这篇博客")
public class NotFoundBlogException extends Exception{

    public NotFoundBlogException(String message){
        super(message);
    }


}
