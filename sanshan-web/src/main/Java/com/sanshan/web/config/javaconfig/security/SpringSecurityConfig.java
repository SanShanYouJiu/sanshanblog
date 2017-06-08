
package com.sanshan.web.config.javaconfig.security;

import com.sanshan.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebMvcSecurity//启动Spring Security功能
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.secret}")
    private String secret;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder())
                .usersByUsernameQuery("SELECT email,password,enabled FROM  user  " +
                        "WHERE email=? ")
                .authoritiesByUsernameQuery("SELECT email,authority FROM authorities" +
                        " WHERE email =?");

    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        //关闭CSRF防护
        httpSecurity.
                csrf().
                disable();

        httpSecurity
                .authorizeRequests()
                .antMatchers(
                        "/**"
                ).permitAll()
                .anyRequest()
                .authenticated();

        //httpSecurity
        //.addFilter();
        // 禁用缓存
        httpSecurity.headers().cacheControl();

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public JwtTokenUtil jwtTokenUtil(){
        JwtTokenUtil util = new JwtTokenUtil();
        util.setExpiration(expiration);
        util.setSecret(secret);
        return  util;
    }


}
