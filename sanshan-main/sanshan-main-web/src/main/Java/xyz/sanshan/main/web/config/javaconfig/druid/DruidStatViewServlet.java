package xyz.sanshan.main.web.config.javaconfig.druid;

import com.alibaba.druid.support.http.StatViewServlet;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

/**
 */
@SuppressWarnings("serial")
@WebServlet(urlPatterns = "/druid/*",
initParams = {
        @WebInitParam(name="loginUsername",value="main"),// 用户名
                @WebInitParam(name="loginPassword",value="123456"),// 密码
})

public class DruidStatViewServlet extends StatViewServlet {


}
