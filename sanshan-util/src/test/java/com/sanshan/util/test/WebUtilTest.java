package com.sanshan.util.test;

import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * HTTP client测试
 */
public class WebUtilTest {
    /**
     * 使用HttpClient 是为了减少三次握手连接带来开销的代价 持久化连接
     *

     */
    @Test
    public  void test(){
        try {
            String url = "https://www.baidu.com";
            /**
             * CloseableHttpClient是基本的HttpClient实现 也实现了关闭
             *  请求参数配置
             *  connectionRequestTimeout:
             *                          从连接池中获取连接的超时时间，超过该时间未拿到可用连接，
             *                          会抛出org.apache.http.conn.ConnectionPoolTimeoutException: Timeout waiting for connection from pool
             *  connectTimeout:
             *                  连接上服务器(握手成功)的时间，超出该时间抛出connect timeout
             *  socketTimeout:
             *                  服务器返回数据(response)的时间，超过该时间抛出read timeout
             */
            CloseableHttpClient client = HttpClients.custom().setDefaultRequestConfig(RequestConfig.custom()
                    .setConnectionRequestTimeout(2000).setConnectTimeout(2000).setSocketTimeout(2000).build()).build();

            //HttpPost post = new HttpPost(url);
            URI uri = new URIBuilder()
                    .setScheme("https")
                    .setHost("www.google.com")
                    .setPath("/search")
                    .setParameter("hl", "zh-CN")
                    .setParameter("q","真的吊")
                    .build();

            HttpPost post = new HttpPost(url);


            //使用Http实体
            //StringEntity myEntity = new StringEntity("important message",
            //        "UTF-8");
            //System.out.println(myEntity.getContentType());
            //System.out.println(myEntity.getContentLength());
            //System.out.println(EntityUtils.getContentCharSet(myEntity));
            //System.out.println(EntityUtils.toString(myEntity));
            //System.out.println(EntityUtils.toByteArray(myEntity).length);

            //streamed流式实体
            //File file = new File("somefile.txt");
            //FileEntity requestEntity = new FileEntity(file, "text/plain; charset=\"UTF-8\"");

            //动态内容实体
            //HttpEntity requestEntity =new EntityTemplate(outstream -> {
            //    Writer writer = new OutputStreamWriter(outstream, "UTF-8");
            //    writer.write("<response>");
            //    writer.write(" <content>");
            //    writer.write(" important stuff");
            //    writer.write(" </content>");
            //    writer.write("</response>");
            //    writer.flush();
            //});

          //模拟提交一个HTML表单
            List<NameValuePair> formparams = new ArrayList<NameValuePair>();
            formparams.add(new BasicNameValuePair("param1", "value1"));
            formparams.add(new BasicNameValuePair("param2", "value2"));
            UrlEncodedFormEntity requestEntity = new UrlEncodedFormEntity(formparams, "UTF-8");

            // 让HttpClient选择基于被传递的HTTP报文属性的最适合的编码转换 也就是在内容分块的时候使用 、
            //内容分块只有在Http 1.1中提供 在没有的版本中会被忽略
            requestEntity.setChunked(true);
            post.setEntity(requestEntity);


            //CloseableHttpResponse response = client.execute(post);

            CloseableHttpResponse response = client.execute(post);
            //处理报文头部
            response.addHeader("Set-Cookie","c1=a;path=/;domain=sanshanyoujiu.github.io");
            response.addHeader("Set-Cookie","c2=b; path=\\\"/\\\", c3=c; domain=\\\"sanshanyoujiu.github.io\\");

            // 服务器返回码
            int status_code = response.getStatusLine().getStatusCode();
            System.out.println("status_code = " + status_code);

            //使用HeadIterator获得给定类型的所有头部信息
            HeaderIterator iterator = response.headerIterator();
            while (iterator.hasNext()){
                System.out.println(iterator.next());
            }
            // 服务器返回内容
            String respStr = null;
            HttpEntity entity = response.getEntity();
            if(entity != null) {
                //需要对应编码
                respStr = EntityUtils.toString(entity, "UTF-8");
            }
            System.out.println("respStr = " + respStr);


            //释放连接 终止请求
            post.abort();
            // 释放资源
            EntityUtils.consume(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
