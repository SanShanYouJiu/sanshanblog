
package xyz.sanshan.web.config.javaconfig.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
import xyz.sanshan.web.config.javaconfig.auxiliary.jwt.JwtAuthenticationEntryPoint;
import xyz.sanshan.web.config.javaconfig.auxiliary.jwt.JwtAuthenticationTokenFilter;

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
                        // 魔法值太多
                        "/api/admin/index/**",
                        "/api/user/change-pwd",
                        "/api/user/email-check",
                        "/api/auth/refresh-token",
                        "/api/auth/login-status",
                        "/api/user/register/check/token",
                        "/api/markdown-editor/**",
                        "/api/file/**",
                        "/api/ueditor-editor/blog",
                        //TODO 将Config与加载图片的代码分离到另外一个Controller中
                        "/api/ueditor-editor/blog/**")
                .hasAnyRole("USER")
                //Spring Boot Actuator 监控
                .antMatchers("/actuator/info",
                        "/actuator/health")
                .permitAll()
                .antMatchers("/actuator/**")
                .hasAnyRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/blog/id/**")
                .hasAnyRole("USER")
                .antMatchers(
                        "/",
                        "/**"
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
