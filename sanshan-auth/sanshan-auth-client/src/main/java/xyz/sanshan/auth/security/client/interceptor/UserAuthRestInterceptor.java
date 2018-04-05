package xyz.sanshan.auth.security.client.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import xyz.sanshan.auth.security.client.annotation.WantUserToken;
import xyz.sanshan.auth.security.client.config.UserAuthConfig;
import xyz.sanshan.auth.security.client.jwt.UserAuthUtil;
import xyz.sanshan.auth.security.common.util.jwt.IJWTInfo;
import xyz.sanshan.common.UserContextHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 */
@Slf4j
public class UserAuthRestInterceptor extends HandlerInterceptorAdapter {

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
        //判断在网关处鉴权
        String token = request.getHeader(userAuthConfig.getTokenHeader());
        if (StringUtils.isEmpty(token)) {
            if (annotation == null) {
                return super.preHandle(request, response, handler);
            }
        }
        //进行拦截
        IJWTInfo infoFromToken = userAuthUtil.getInfoFromToken(token);
        UserContextHandler.setUsername(infoFromToken.getUsername());
        UserContextHandler.setUserID(infoFromToken.getId());
        return super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }
}
