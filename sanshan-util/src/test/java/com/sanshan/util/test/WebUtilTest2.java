package com.sanshan.util.test;

import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.routing.RouteInfo;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParamBean;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;

import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Http Client测试
 */
public class WebUtilTest2 {

    public static void main(String[] args) throws IOException {

         //默认实现了HttpClient  预配置了大部分实现场景
        DefaultHttpClient httpclient = new DefaultHttpClient();

        //HTTP上下文
        HttpContext localContext = new BasicHttpContext();

        //HttpClientContext适配器类来简化与上下文状态之间的相互作用
        HttpClientContext clientContext = HttpClientContext.adapt(localContext);
        AtomicInteger count = new AtomicInteger(1);
        localContext.setAttribute("count", count);

        /**
         * HttpClient会试图自动从I/O异常中恢复。默认的自动恢复机制是受很少一部分已知的异常是安全的这个限制。
         * HttpClient不会从任意逻辑或HTTP协议错误（那些是从HttpException类中派生出的）中恢复的。
         * HttpClient将会自动重新执行那么假设是幂等的方法。

         *(HTTP/1.1 明确地定义了幂等的方法，描述如下
         [方法也可以有“幂等”属性在那些（除了错误或过期问题）N的副作用>0的相同请求和独立的请求是相同的]
         换句话说，应用程序应该保证准备着来处理多个相同方法执行的实现。这是可以达到的，比如，通过提供一个独立的事务ID和其它避免执行相同逻辑操作的方法。
         请注意这个问题对于HttpClient是不具体的。基于应用的浏览器特别受和非幂等的HTTP方法相关的相同问题的限制。
         HttpClient假设没有实体包含方法，比如GET和HEAD是幂等的，而实体包含方法，比如POST和PUT则不是。)

         * HttpClient将会自动重新执行那些由于运输异常失败，而HTTP请求仍然被传送到目标服务器（也就是请求没有完全被送到服务器）失败的方法。
         * HttpClient将会自动重新执行那些已经完全被送到服务器，但是服务器使用HTTP状态码（服务器仅仅丢掉连接而不会发回任何东西）响应时失败的方法。在这种情况下，假设请求没有被服务器处理，而应用程序的状态也没有改变。如果这个假设可能对于你应用程序的目标Web服务器来说不正确，那么就强烈建议提供一个自定义的异常处理器。
         * 提供一个HttpRequestRetryHandler接口进行请求重试处理
         *
         */
        httpclient.setHttpRequestRetryHandler((exception, executionCount, context) -> {
            if (executionCount >= 5) {
        // 如果超过最大重试次数，那么就不要继续了
                return false;
            }
            if (exception instanceof NoHttpResponseException) {
          // 如果服务器丢掉了连接，那么就重试
                return true;
            }
            if (exception instanceof SSLHandshakeException) {
        // 不要重试SSL握手异常
                return false;
            }
            HttpRequest request = (HttpRequest) context.getAttribute(
                    ExecutionContext.HTTP_REQUEST);
            boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
            if (idempotent) {
                // 如果请求被认为是幂等的，那么就重试
                return true;
            }
            return false;
        });

        //协议拦截器也可以通过共享信息来共同合作-比如处理状态-通过HTTP执行上下文。协议拦截器可以使用HTTP内容来为一个或多个连续的请求存储一个处理状态。
        httpclient.addRequestInterceptor(new HttpRequestInterceptor() {
            public void process(final HttpRequest request,
                                final HttpContext context) throws HttpException, IOException {
                HttpResponse response = clientContext.getResponse();
                //路由
                RouteInfo info = clientContext.getHttpRoute();
                //request配置项
                RequestConfig config = clientContext.getRequestConfig();
                System.out.println(info);

                AtomicInteger count = (AtomicInteger) context.getAttribute("count");
                System.out.println(context.getAttribute("count"));
                //检测当前Http执行环境
                System.out.println(context.getAttribute(ExecutionContext.HTTP_TARGET_HOST));
                request.addHeader("Count", Integer.toString(count.getAndIncrement()));
            }
        });


        HttpGet httpget = new HttpGet("https://www.baidu.com/");

        //这是在HTTP客户端请求级别的参数
        HttpParams params_client = httpclient.getParams();
        //这是在HTTP请求级别的参数
        HttpParams params = httpget.getParams();

        // HTTP参数bean 用于DI框架组合
        HttpProtocolParamBean paramsBean = new HttpProtocolParamBean(params);

        paramsBean.setVersion(HttpVersion.HTTP_1_1);
        paramsBean.setContentCharset("UTF-8");
        paramsBean.setUseExpectContinue(true);
        System.out.println(params.getParameter(
                CoreProtocolPNames.PROTOCOL_VERSION));
        System.out.println(params.getParameter(
                CoreProtocolPNames.HTTP_CONTENT_CHARSET));
        System.out.println(params.getParameter(
                CoreProtocolPNames.USE_EXPECT_CONTINUE));
        System.out.println(params.getParameter(
                CoreProtocolPNames.USER_AGENT));
        /**
         * 这些参数会影响到请求执行的过程：
         'http.protocol.version'：如果没有在请求对象中设置明确的版本信息，它就定义了使用的HTTP协议版本。这个参数期望得到一个ProtocolVersion类型的值。如果这个参数没有被设置，那么就使用HTTP/1.1。
         'http.protocol.element-charset'：定义了编码HTTP协议元素的字符集。这个参数期望得到一个java.lang.String类型的值。如果这个参数没有被设置，那么就使用US-ASCII。
         'http.protocol.eontent-charset'：定义了为每个内容主体编码的默认字符集。这个参数期望得到一个java.lang.String类型的值。如果这个参数没有被设置，那么就使用ISO-8859-1。
         'http.useragent'：定义了头部信息User-Agent的内容。这个参数期望得到一个java.lang.String类型的值。如果这个参数没有被设置，那么HttpClient将会为它自动生成一个值。
         'http.protocol.strict-transfer-encoding'：定义了响应头部信息中是否含有一个非法的Transfer-Encoding，都要拒绝掉。
         'http.protocol.expect-continue'：为包含方法的实体激活Expect: 100-Continue握手。Expect: 100-Continue握手的目的是允许客户端使用请求体发送一个请求信息来决定源服务器是否希望在客户端发送请求体之前得到这个请求（基于请求头部信息）。Expect: 100-Continue握手的使用可以对需要目标服务器认证的包含请求的实体（比如POST和PUT）导致明显的性能改善。Expect: 100-Continue握手应该谨慎使用，因为它和HTTP服务器，不支持HTTP/1.1协议的代理使用会引起问题。这个参数期望得到一个java.lang.Boolean类型的值。如果这个参数没有被设置，那么HttpClient将会试图使用握手。
         'http.protocol.wait-for-continue'：定义了客户端应该等待100-Continue响应最大的毫秒级时间间隔。这个参数期望得到一个java.lang.Integer类型的值。如果这个参数没有
         */


        httpget.setProtocolVersion(HttpVersion.HTTP_1_1);
        for (int i = 0; i < 10; i++) {
            HttpResponse response = null;
            try {
                response = httpclient.execute(httpget, localContext);
            } catch (IOException e) {
                e.printStackTrace();
            }
            HttpEntity entity = response.getEntity();
            //System.out.println(EntityUtils.toString(entity,"UTF-8"));
            if (entity != null) {
                try {
                    entity.consumeContent();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
