package com.sanshan.web.config.webaseconfig;

import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[]{RootConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[]{WebConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/api/*"};
    }


//    @Override
//    protected void customizeRegistration(ServletRegistration.Dynamic registration) {
//        registration.setMultipartConfig(
//                new MultipartConfigElement("D://测试", 2097152,4194304,0));
//    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
         //
        FilterRegistration.Dynamic filter = servletContext.addFilter("CharacterEncodingFilter",
                CharacterEncodingFilter.class);
        filter.setInitParameter("encoding", "utf-8");
        filter.addMappingForUrlPatterns(null, false, "/api/*");

//        主要负责处理由　JavaBeans Introspector的使用而引起的缓冲泄露
        servletContext.addListener(org.springframework.web.util.IntrospectorCleanupListener.class);
//
        //log4j监听器
        servletContext.addListener(org.springframework.web.util.Log4jConfigListener.class);

        //Druid监控
        ServletRegistration.Dynamic druidservlet = servletContext.addServlet("DruidStatView", com.alibaba.druid.support.http.StatViewServlet.class);
        druidservlet.addMapping("/druid/*");

        super.onStartup(servletContext);
    }


}