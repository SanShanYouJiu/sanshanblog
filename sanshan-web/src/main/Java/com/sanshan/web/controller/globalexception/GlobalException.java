package com.sanshan.web.controller.globalexception;

import com.sanshan.util.exception.ERROR;
import com.sanshan.util.exception.IdMapWriteException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
@RestController
@Slf4j
public class GlobalException {

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.OK)//前端不支持非200的异常
    public ERROR NotFoundExceptionHandler(NullPointerException e) {
        log.error("NullPointerException  error occurred:" + e.getMessage());
        return new ERROR(500, "出错 NullPointerException空指针异常:" + e +
                " message:" + e.getMessage()
                + "  cause:" + e.getCause());
    }


    @ExceptionHandler(IdMapWriteException.class)
    @ResponseStatus(HttpStatus.OK)
    public ERROR IdMapSaveExceptionHandler(Exception e) {
        log.error("IdMapWriteException  error occurred:" + e.getMessage());
        return new ERROR(500, "IO出错 IdMap写入错误");
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public Object ExceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        log.error("exception handler "+ex);


        //重定向到错误页面
        request.setAttribute("errorMessage",ex.getMessage());
        redirect("/error",HttpStatus.NOT_FOUND,request,response);
        return null;
    }




    /**
     * 重定向到错误页面
     * @param url 链接
     */
    private void redirect(String url,HttpStatus status,HttpServletRequest request,
                          HttpServletResponse response){
        try {
            response.setStatus(status.value());
            request.getRequestDispatcher(url).forward(request,response);
        } catch (IOException | ServletException e) {
            log.error("redirect fail,e:{}",e);
        }
    }


}
