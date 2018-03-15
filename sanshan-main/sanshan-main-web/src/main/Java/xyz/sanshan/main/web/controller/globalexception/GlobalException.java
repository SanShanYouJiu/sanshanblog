package xyz.sanshan.main.web.controller.globalexception;

import xyz.sanshan.main.service.vo.ResponseMsgVO;
import xyz.sanshan.common.AbstractWebUtils;
import xyz.sanshan.common.exception.IdMapWriteException;
import xyz.sanshan.common.info.PosCodeEnum;
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

    public static final String ERROR_MSG = "errorMessage";

    public static final  String ERROR_URL="/error";

    @ExceptionHandler(IdMapWriteException.class)
    @ResponseStatus(HttpStatus.OK)
    public ResponseMsgVO idMapSaveExceptionHandler(Exception e) {
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        return responseMsgVO.buildWithMsgAndStatus(PosCodeEnum.INTER_ERROR, "IO出错 IdMap写入错误:" + e.getMessage());
    }


    //TODO 拦住exception不是一个好选择
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public Object exceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception ex) throws ServletException, IOException {

        //TODO 目前前端用的不是ajax
        //判断是否为ajax请求
        String xRequested = AbstractWebUtils.getHeader("x-requested-with",null,request);
        if (StringUtils.equalsIgnoreCase(xRequested,"XMLHttpRequest")){
            return handlerAjax(ex);
        }

        //转发到错误页面
        request.setAttribute(ERROR_MSG,ex.getMessage());
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
                          HttpServletResponse response)   {
            response.setStatus(status.value());
        try {
            request.getRequestDispatcher(url).forward(request,response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
