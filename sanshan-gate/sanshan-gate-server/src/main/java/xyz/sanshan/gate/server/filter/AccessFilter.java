package xyz.sanshan.gate.server.filter;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import xyz.sanshan.auth.security.client.config.ServiceAuthConfig;
import xyz.sanshan.auth.security.client.config.UserAuthConfig;
import xyz.sanshan.auth.security.client.jwt.ServiceAuthUtil;
import xyz.sanshan.auth.security.client.jwt.UserAuthUtil;
import xyz.sanshan.auth.security.common.util.jwt.IJWTInfo;
import xyz.sanshan.common.UserContextHandler;
import xyz.sanshan.common.exception.auth.UserTokenException;
import xyz.sanshan.common.info.HttpMethodEnum;
import xyz.sanshan.common.vo.PermissionInfo;
import xyz.sanshan.gate.server.auth.AuthPermissionUrlUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
public class AccessFilter extends ZuulFilter {

    private AntPathMatcher matcher = new AntPathMatcher();

    @Value("${zuul.prefix}")
    private String zuulPrefix;

    @Autowired
    private AuthPermissionUrlUtil authPermissionUrlUtil;

    @Autowired
    private ServiceAuthUtil serviceAuthUtil;
    @Autowired
    private UserAuthUtil userAuthUtil;
    @Autowired
    private UserAuthConfig userAuthConfig;

    @Autowired
    private ServiceAuthConfig serviceAuthConfig;


    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        final String requestUri = request.getRequestURI().substring(zuulPrefix.length());
        final String method = request.getMethod();
        UserContextHandler.setToken(null);

        //拦截地址过滤
        //TODO: 2018:4:4  将分散在各个子系统的资源权限集中到这里进行审核 RBAC模型
        boolean userAuthDetection = userAuthDetection(requestUri, method);
        if (userAuthDetection) {
            //需要用户权限
            IJWTInfo user = null;
            try {
                user = getJWTUser(request, ctx);
            } catch (UserTokenException e) {
                setFailedRequest(JSON.toJSONString(e.getMessage()), 401);
                return null;
            } catch (Exception e) {
                setFailedRequest(JSON.toJSONString(e.getMessage()), 200);
                return null;
            }
        }
        boolean adminAuthDetection = adminAuthDetection(requestUri, method);
        if (adminAuthDetection) {
            //管理权限暂不开放
            setFailedRequest(JSON.toJSONString("Wrong URL to access"), 401);
            return null;
        }

        // 申请客户端密钥头
        ctx.addZuulRequestHeader(serviceAuthConfig.getTokenHeader(), serviceAuthUtil.getClientToken());
        return null;
    }

    /**
     * 返回token中的用户信息
     *
     * @param request
     * @param ctx
     * @return
     */
    private IJWTInfo getJWTUser(HttpServletRequest request, RequestContext ctx) throws Exception {
        String authToken = request.getHeader(userAuthConfig.getTokenHeader());
        if (StringUtils.isBlank(authToken)) {
            authToken = request.getParameter("token");
        }
        ctx.addZuulRequestHeader(userAuthConfig.getTokenHeader(), authToken);
        UserContextHandler.setToken(authToken);
        return userAuthUtil.getInfoFromToken(authToken);
    }

    /**
     * 是否需要用户权限才能访问
     *
     * @param requestUrl
     * @param method
     * @return
     */
    private boolean userAuthDetection(String requestUrl, String method) {
        boolean flag = false;
        Map<String, PermissionInfo> userAllowUrl = authPermissionUrlUtil.getUserAllowUrl();
        Set<PermissionInfo> userAntPatternAllowUrl = authPermissionUrlUtil.getUserAntPatternAllowUrl();
        PermissionInfo userPermission = urlMatch(userAllowUrl, userAntPatternAllowUrl, requestUrl);
        //进行用户权限检查
        if (userPermission != null) {
            HttpMethodEnum httpMethodEnum = userPermission.getMethod();
            if (httpMethodEnum.matches(method)) {
                //需要
                return true;
            }
        }
        return flag;
    }

    /**
     * 是否需要管理权限才能访问
     *
     * @param requestUrl
     * @param method
     * @return
     */
    private boolean adminAuthDetection(String requestUrl, String method) {
        boolean flag = false;
        Map<String, PermissionInfo> adminAllowUrl = authPermissionUrlUtil.getAdminAllowUrl();
        Set<PermissionInfo> antPatternAllowUrl = authPermissionUrlUtil.getAdminAntPatternAllowUrl();
        //URL 匹配
        PermissionInfo adminPermission = urlMatch(adminAllowUrl, antPatternAllowUrl, requestUrl);
        //进行用户权限检查
        if (adminPermission != null) {
            HttpMethodEnum httpMethodEnum = adminPermission.getMethod();
            if (httpMethodEnum.matches(method)) {
                //需要
                return true;
            }
        }
        return flag;
    }

    /**
     * 进行URL匹配
     * @param allowUrls
     * @param antUrls
     * @param requestUrl
     * @return
     */
    private PermissionInfo urlMatch(Map<String, PermissionInfo> allowUrls, Set<PermissionInfo> antUrls, String requestUrl) {
        PermissionInfo permissionInfo = allowUrls.get(requestUrl);
        if (permissionInfo != null) {
            return permissionInfo;
        } else {
            //ant 匹配
            Iterator<PermissionInfo> iterator= antUrls.iterator();
            while (iterator.hasNext()) {
               PermissionInfo info= iterator.next();
                if (matcher.match(info.getUri(), requestUrl)) {
                    return info;
                }
            }
        }
        return null;
    }


    /**
     * 网关抛异常
     *
     * @param body
     * @param code
     */
    private void setFailedRequest(String body, int code) {
        log.debug("Reporting error ({}): {}", code, body);
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.setResponseStatusCode(code);
        if (ctx.getResponseBody() == null) {
            ctx.setResponseBody(body);
            ctx.setSendZuulResponse(false);
        }
    }
}
