package xyz.sanshan.auth.security.client.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import xyz.sanshan.auth.security.client.annotation.WantClientToken;
import xyz.sanshan.auth.security.client.config.ServiceAuthConfig;
import xyz.sanshan.auth.security.client.jwt.ServiceAuthUtil;
import xyz.sanshan.auth.security.common.util.jwt.IJWTInfo;
import xyz.sanshan.common.exception.auth.ClientForbiddenException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class ServiceAuthInterceptor extends HandlerInterceptorAdapter {


    @Autowired
    private ServiceAuthUtil serviceAuthUtil;

    @Autowired
    private ServiceAuthConfig serviceAuthConfig;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        WantClientToken annotation = handlerMethod.getBeanType().getAnnotation(WantClientToken.class);
        if (annotation == null) {
            annotation = handlerMethod.getMethodAnnotation(WantClientToken.class);
        }
        //判断在网关处鉴权
        String token = request.getHeader(serviceAuthConfig.getTokenHeader());
        if (StringUtils.isEmpty(token)) {
            if (annotation == null) {
                return super.preHandle(request, response, handler);
            }
        }
        IJWTInfo infoFromToken = serviceAuthUtil.getInfoFromToken(token);
        //客户端的名字
        String username = infoFromToken.getUsername();
        for(String client:serviceAuthUtil.getAllowedClient()){
            if(client.equals(username)){
                return super.preHandle(request, response, handler);
            }
        }
        throw new ClientForbiddenException("Client is Forbidden!");
    }

}
