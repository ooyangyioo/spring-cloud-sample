//package org.yangyi.project.oauth.config;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
//import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
//
//@Configuration
//@EnableResourceServer
//public class AuthResourceServerConfigurer extends ResourceServerConfigurerAdapter {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(AuthResourceServerConfigurer.class);
//
//    /**
//     * /api/下所有的请求都需要认证
//     */
//    @Override
//    public void configure(HttpSecurity http) throws Exception {
//        http.exceptionHandling()    //  异常处理(权限拒绝、登录失效等)
//                .authenticationEntryPoint((request, response, authException) -> {
//                    LOGGER.warn("认证失败");
//                    response.setContentType("text/json;charset=utf-8");
//                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
//                    response.getWriter().write("认证失败");
//                })  //  匿名用户访问无权限资源时的异常处理
//                .and().requestMatchers().antMatchers("/api/**", "/actuator/**", "/user/register")
//                .and().authorizeRequests().antMatchers("/actuator/**").permitAll()
//                .and().authorizeRequests().antMatchers("/user/register").permitAll()
//                .and().authorizeRequests().antMatchers("/api/**").access("#oauth2.hasScope('read')");
//    }
//
//}
