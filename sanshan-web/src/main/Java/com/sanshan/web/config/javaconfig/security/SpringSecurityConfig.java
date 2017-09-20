
package com.sanshan.web.config.javaconfig.security;

import com.sanshan.web.config.javaconfig.auxiliary.jwt.JwtAuthenticationEntryPoint;
import com.sanshan.web.config.javaconfig.auxiliary.jwt.JwtAuthenticationTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@SuppressWarnings("SpringJavaAutowiringInspection")
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Autowired
    private UserDetailsService userDetailsService;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(this.userDetailsService)
                .passwordEncoder(passwordEncoder());

    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public JwtAuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
        return new JwtAuthenticationTokenFilter();
    }


    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        // 由于使用的是JWT，我们这里不需要csrf
        httpSecurity.
                csrf().
                disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                // 基于token，所以不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(
                        //TODO 魔法值太多
                        "/api/admin/index/**",
                        "/api/user/change-pwd",
                        "/api/user/email-check",
                        "/api/auth/refresh",
                        "/api/register/check/token",
                        "/api/blog/delete-by-id",
                        "/api/markdown-editor/**",
                        "/api/ueditor-editor/query-by-page",//TODO 将Config与加载图片的代码分离到另外一个Controller中
                        "/api/ueditor-editor/query-all",
                        "/api/ueditor-editor/insert-blog",
                        "/api/ueditor-editor/delete-by-id",
                        "/api/ueditor-editor/update-by-id")
                .hasAnyRole("USER")
                .antMatchers(
                        "/",
                        //搜索引擎
                        "/solr/**",
                        "/api/",
                        "/favicon.ico",
                        "/api/blog/**",
                        "/druid/**",
                        "/api/codeValidate",
                        "/api/index/**",
                        //不知道如何在前端的Ueditor端加入Authorization属性
                        "/api/ueditor-editor/config",
                        "/api/ueditor-editor/upload/**",
                        "/api/auth/login",
                        "/api/auth/register",
                        "/api/user/email/send",
                        "/api/user/check/token",
                        "/api/user/forget-pwd",
                        "/api/file/**"
                ).permitAll()
                .anyRequest()
                .authenticated();


        // 添加JWT filter
        httpSecurity
                .addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
        // 禁用缓存
        httpSecurity.headers().cacheControl();

    }

}
