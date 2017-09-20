package com.sanshan.web.controller.globalexception;

import com.sanshan.service.vo.ResponseMsgVO;
import com.sanshan.util.WebUtils;
import com.sanshan.util.exception.IdMapWriteException;
import com.sanshan.util.info.PosCodeEnum;
import org.apache.commons.lang3.StringUtils;
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
public class GlobalException {

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.OK)//前端不支持非200的异常
    public ResponseMsgVO NotFoundExceptionHandler(NullPointerException e) {
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        return responseMsgVO.buildWithMsgAndStatus(PosCodeEnum.INTER_ERROR, "出错 NullPointerException空指针异常 可能是没有对应的ID的Blog 或是没有权限执行此操作" +
                ":" + e +
                " message:" + e.getMessage()
                + "  cause:" + e.getCause());
    }


    @ExceptionHandler(IdMapWriteException.class)
    @ResponseStatus(HttpStatus.OK)
    public ResponseMsgVO IdMapSaveExceptionHandler(Exception e) {
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        return responseMsgVO.buildWithMsgAndStatus(PosCodeEnum.INTER_ERROR, "IO出错 IdMap写入错误:" + e.getMessage());
    }


    //拦住exception不是一个好选择
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public Object ExceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception ex) throws ServletException, IOException {

        //TODO 目前前端用的不是ajax
        //判断是否为ajax请求
        String xRequested = WebUtils.getHeader("x-requested-with",null,request);
        if (StringUtils.equalsIgnoreCase(xRequested,"XMLHttpRequest")){
            return handlerAjax(ex);
        }

        //转发到错误页面
        request.setAttribute("errorMessage",ex.getMessage());
        redirect("/api/error",HttpStatus.NOT_FOUND,request,response);
        return null;
    }


    /**
     * 处理ajax异常
     * @param ex 异常
     * @return json异常信息
     */
    private ResponseMsgVO handlerAjax(Exception ex){
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        return responseMsgVO.buildWithMsgAndStatus(PosCodeEnum.NO_PRIVILEGE,ex.getMessage());
    }


    /**
     * 转发到错误页面
     * @param url 链接
     */
    private void redirect(String url,HttpStatus status,HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
            response.setStatus(status.value());
            request.getRequestDispatcher(url).forward(request,response);
    }

}
