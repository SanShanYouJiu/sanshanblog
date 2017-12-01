package com.sanshan.web.config.javaconfig.auxiliary;

import com.sanshan.service.vo.ResponseMsgVO;
import com.sanshan.util.exception.CheckException;
import com.sanshan.util.exception.FileLoadException;
import com.sanshan.util.info.PosCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.ResponseEntity;

/**
 * 处理和包装异常
 */
@Slf4j
@Aspect
public class ControllerAop {


    @Pointcut("execution(public com.sanshan.service.vo.ResponseMsgVO *(..))")
    public void ResponseMsgVoPointcut() {
    }

    //在加载文件的FileLoadController中使用了ResponseEntity<Resources>
    @Pointcut("execution(public org.springframework.http.ResponseEntity *(..))")
    public void ResponseEntityPointcut(){
    }


    @Around("ResponseMsgVoPointcut()")
    public Object handlerControllerMethod(ProceedingJoinPoint pjp) {
        long startTime = System.currentTimeMillis();

        ResponseMsgVO<?> result;

        try {
            result = (ResponseMsgVO<?>) pjp.proceed();
            log.info("method:"+pjp.getSignature() + " use time:" + (System.currentTimeMillis() - startTime)+"ms");
        } catch (Throwable e) {
            result = handlerException(pjp, e);
            log.info("method:"+pjp.getSignature() + " use time:" + (System.currentTimeMillis() - startTime)+"ms");
        }

        return result;
    }


    @Around("ResponseEntityPointcut()")
    public Object handlerControllerFileMethod(ProceedingJoinPoint pjp) {
        long startTime = System.currentTimeMillis();

        ResponseEntity<?> result ;

        try {
            result = (ResponseEntity<?>) pjp.proceed();
            log.info("method:"+pjp.getSignature() + " use time:" + (System.currentTimeMillis() - startTime)+"ms");
        } catch (Throwable e) {
            result = handlerFileException(pjp, e);
            log.info("method:"+pjp.getSignature() + " use time:" + (System.currentTimeMillis() - startTime)+"ms");
        }

        return result;
    }


    private ResponseMsgVO<?> handlerException(ProceedingJoinPoint pjp, Throwable e) {
        ResponseMsgVO<?> result = new ResponseMsgVO();

        // 已知异常
        if (e instanceof CheckException) {
            result.setMsg(e.getLocalizedMessage());
            result.setStatus(PosCodeEnum.INTER_ERROR.getStatus());
        //} else if (e instanceof UnloginException) {
            //result.setMsg("Unlogin");
            //result.setCode(ResultBean.NO_LOGIN);
        } else {
            log.error(pjp.getSignature() + " error ", e);
            //TODO 未知的异常，应该格外注意，可以发送邮件通知等
            result.setMsg(e.toString());
            result.setStatus(PosCodeEnum.INTER_ERROR.getStatus());
        }

        return result;
    }


    private ResponseEntity<?> handlerFileException(ProceedingJoinPoint pjp, Throwable e) {
        ResponseEntity<?> result;
        ResponseMsgVO msgVO = null;

        if (e instanceof FileLoadException){
           msgVO.buildWithMsgAndStatus(PosCodeEnum.INTER_ERROR,"文件加载错误");
            result= ResponseEntity.badRequest().body(msgVO);
        }else {
            log.error(pjp.getSignature() + " error ", e);
            //TODO 未知的异常，应该格外注意，可以发送邮件通知等
           result= ResponseEntity.badRequest().body(msgVO.buildWithPosCode(PosCodeEnum.UNKONW_EXCEPTION));
        }
        return result;
    }

}
