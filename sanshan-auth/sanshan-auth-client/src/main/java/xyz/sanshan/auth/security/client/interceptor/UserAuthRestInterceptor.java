package xyz.sanshan.auth.security.client.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import xyz.sanshan.auth.security.client.annotation.WantUserToken;
import xyz.sanshan.auth.security.client.config.UserAuthConfig;
import xyz.sanshan.auth.security.client.jwt.UserAuthUtil;
import xyz.sanshan.auth.security.common.util.jwt.IJWTInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 */
public class UserAuthRestInterceptor extends HandlerInterceptorAdapter {
    private Logger logger = LoggerFactory.getLogger(UserAuthRestInterceptor.class);

    @Autowired
    private UserAuthUtil userAuthUtil;

    @Autowired
    private UserAuthConfig userAuthConfig;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        // 配置该注解，说明进行用户拦截
        WantUserToken annotation = handlerMethod.getBeanType().getAnnotation(WantUserToken.class);
        if (annotation==null){
            annotation = handlerMethod.getMethodAnnotation(WantUserToken.class);
        }
        if (annotation == null) {
            return super.preHandle(request, response, handler);
        }
        String token = request.getHeader(userAuthConfig.getTokenHeader());
        if (StringUtils.isEmpty(token)) {
         //
        }
        IJWTInfo infoFromToken = userAuthUtil.getInfoFromToken(token);
        return super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }
}
