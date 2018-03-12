package xyz.sanshan.web.controller;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import xyz.sanshan.common.info.PosCodeEnum;
import xyz.sanshan.service.vo.ResponseMsgVO;
import xyz.sanshan.web.controller.globalexception.GlobalException;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;


@RestController
@RequestMapping("/")
public class  IndexController implements ErrorController {

    private static final String PATH = GlobalException.ERROR_URL;

    @RequestMapping()
    public ModelAndView index() {
        return new ModelAndView("index");
    }

    /**
     * 只适用于应用程序的自定义的异常信息抛出
     * 而不能对框架内部的异常进行处理
     * @param request
     * @return
     */
    @RequestMapping(value = PATH,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO error(HttpServletRequest request){
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        String errorMsg ;
        if (Objects.isNull( request.getAttribute(GlobalException.ERROR_MSG))){
            errorMsg="请求错误";
        }else {
            errorMsg= (String) request.getAttribute(GlobalException.ERROR_MSG);
        }
       return responseMsgVO.buildWithMsgAndStatus(PosCodeEnum.INTER_ERROR, errorMsg);
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }
}