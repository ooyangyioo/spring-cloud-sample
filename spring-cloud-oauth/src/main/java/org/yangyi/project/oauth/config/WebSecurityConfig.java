package org.yangyi.project.oauth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.yangyi.project.oauth.handler.LoginAuthenticationEntryPoint;
import org.yangyi.project.oauth.handler.LoginFailureHandler;
import org.yangyi.project.oauth.handler.LoginSuccessHandler;
import org.yangyi.project.oauth.util.ResponseUtil;
import org.yangyi.project.oauth.vo.ResponseVO;

/**
 * 启动基于Spring Security的安全认证
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String LOGIN_PAGE = "http://localhost/oauth.html";
    private static final String LOGIN_PROCESSING_URL = "/user/login";
    private static final String FORM_USERNAME_KEY = "username";
    private static final String FORM_PASSWORD_KEY = "password";

    private LoginSuccessHandler loginSuccessHandler;
    private LoginFailureHandler loginFailureHandler;
    private LoginAuthenticationEntryPoint loginAuthenticationEntryPoint;

    @Autowired
    public void setLoginSuccessHandler(LoginSuccessHandler loginSuccessHandler) {
        this.loginSuccessHandler = loginSuccessHandler;
    }

    @Autowired
    public void setLoginFailureHandler(LoginFailureHandler loginFailureHandler) {
        this.loginFailureHandler = loginFailureHandler;
    }

    @Autowired
    public void setLoginAuthenticationEntryPoint(LoginAuthenticationEntryPoint loginAuthenticationEntryPoint) {
        this.loginAuthenticationEntryPoint = loginAuthenticationEntryPoint;
        this.loginAuthenticationEntryPoint.setRedirectUrl(LOGIN_PAGE);
        this.loginAuthenticationEntryPoint.setUseForward(true);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().cors();

        http.formLogin()
                .loginPage(LOGIN_PAGE)
                .usernameParameter(FORM_USERNAME_KEY)  //  设置登录账号参数，与表单参数一致
                .passwordParameter(FORM_PASSWORD_KEY)  //  设置登录密码参数，与表单参数一致
                .loginProcessingUrl(LOGIN_PROCESSING_URL);

        http.formLogin()
                .successHandler(loginSuccessHandler)  //  登录成功处理器
                .failureHandler(loginFailureHandler)  //  登录失败处理器
                .permitAll();

        http.exceptionHandling()
                .authenticationEntryPoint(loginAuthenticationEntryPoint) //  匿名用户访问无权限资源时的异常处理
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    ResponseUtil.forbiddenResponse(response, new ResponseVO("0", "无访问权限！", null));
                });   //  没有权限处理

        http.authorizeRequests().antMatchers("/oauth/**").fullyAuthenticated()
                .and().authorizeRequests().requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()
                .and().authorizeRequests().antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .anyRequest().authenticated();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                "/webjars/**",
                "/**/*.js",
                "/**/*.css",
                "/static/**",
                "/favicon.ico"
        );
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
