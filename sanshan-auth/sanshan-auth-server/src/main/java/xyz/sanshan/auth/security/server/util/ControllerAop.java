package xyz.sanshan.auth.security.server.util;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import xyz.sanshan.common.exception.CheckException;
import xyz.sanshan.common.info.PosCodeEnum;
import xyz.sanshan.common.vo.ResponseMsgVO;

/**
 * 处理和包装异常
 */
@Aspect
@Slf4j
public class ControllerAop {


    @Pointcut("execution(public xyz.sanshan.common.vo.ResponseMsgVO *(..))")
    public void responseMsgVoPointcut() {
    }


    @Around("responseMsgVoPointcut()")
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


    private ResponseMsgVO<?> handlerException(ProceedingJoinPoint pjp, Throwable e) {
        ResponseMsgVO<?> result = new ResponseMsgVO();

        // 已知异常
        if (e instanceof CheckException) {
            result.setMsg(e.getLocalizedMessage());
            result.setStatus(((CheckException) e).getStatus());
        } else {
            log.error(pjp.getSignature() + " error ", e);
            //未知的异常，应该格外注意，可以发送邮件通知等 生产环境关闭
            result.setMsg(e.toString());
            result.setStatus(PosCodeEnum.UNKONW_EXCEPTION.getStatus());
        }

        return result;
    }


}
