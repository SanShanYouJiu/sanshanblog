
package xyz.sanshan.main.web.config.javaconfig.security;

@SuppressWarnings("SpringJavaAutowiringInspection")
//@Configuration
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfig /*extends WebSecurityConfigurerAdapter*/ {
    //
    //@Autowired
    //private JwtAuthenticationEntryPoint unauthorizedHandler;
    //
    //@Autowired
    //private UserDetailsService userDetailsService;
    //
    //
    //@Override
    //protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    //    auth
    //            .userDetailsService(this.userDetailsService)
    //            .passwordEncoder(passwordEncoder());
    //
    //}
    //
    //@Bean
    //@Override
    //public AuthenticationManager authenticationManagerBean() throws Exception {
    //    return super.authenticationManagerBean();
    //}
    //
    //@Bean
    //public PasswordEncoder passwordEncoder() {
    //    return new BCryptPasswordEncoder();
    //}
    //
    //
    //@Bean
    //public JwtAuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
    //    return new JwtAuthenticationTokenFilter();
    //}
    //
    //@Override
    //protected void configure(HttpSecurity httpSecurity) throws Exception {
    //    // 由于使用的是JWT，我们这里不需要csrf
    //    httpSecurity.
    //            csrf().
    //            disable()
    //            .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
    //            // 基于token，所以不需要session
    //            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    //            .and()
    //            .authorizeRequests()
    //            .antMatchers(
    //                    // 魔法值太多
    //                    "/admin/index/**",
    //                    "/user/change-pwd",
    //                    "/user/email-check",
    //                    "/auth/refresh-token",
    //                    "/auth/login-status",
    //                    "/user/register/check/token",
    //                    "/markdown-editor/**",
    //                    "/file/**",
    //                    "/ueditor-editor/blog",
    //                    "/ueditor-editor/blog/**")
    //            .hasAnyRole("USER")
    //            //Spring Boot Actuator 监控
    //            .antMatchers("/actuator/info",
    //                    "/actuator/health")
    //            .permitAll()
    //            .antMatchers("/actuator/**")
    //            .hasAnyRole("ADMIN")
    //            .antMatchers(HttpMethod.DELETE, "/blog/id/**")
    //            .hasAnyRole("USER")
    //            .antMatchers(
    //                    "/",
    //                    "/**"
    //            ).permitAll()
    //            .anyRequest()
    //            .authenticated();
    //
    //
    //    // 添加JWT filter
    //    httpSecurity
    //            .addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
    //    // 禁用缓存
    //    httpSecurity.headers().cacheControl();
    //
    //}

}
