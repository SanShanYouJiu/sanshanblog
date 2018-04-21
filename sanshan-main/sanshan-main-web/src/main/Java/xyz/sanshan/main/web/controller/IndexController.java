package xyz.sanshan.main.web.controller;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import xyz.sanshan.common.info.PosCodeEnum;
import xyz.sanshan.common.vo.ResponseMsgVO;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/")
public class  IndexController implements ErrorController {

    private static final String PATH = "/error";

    @RequestMapping()
    public ModelAndView index() {
        return new ModelAndView("index");
    }

    /**
     *  异常信息抛出
     * @param request
     * @return
     */
    @RequestMapping(value = PATH,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO error(HttpServletRequest request){
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        String errorMsg ="请求错误";
       return responseMsgVO.buildWithMsgAndStatus(PosCodeEnum.INTER_ERROR, errorMsg);
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }
}