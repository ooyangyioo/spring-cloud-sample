package org.yangyi.project.gateway.config;

import cn.hutool.core.util.ArrayUtil;
import org.springframework.boot.actuate.autoconfigure.security.reactive.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.yangyi.project.gateway.token.GatewayAuthorizationManager;
import org.yangyi.project.gateway.handler.GatewayAccessDeniedHandler;
import org.yangyi.project.gateway.handler.GatewayAuthenticationEntryPoint;
import org.yangyi.project.gateway.handler.GatewayAuthenticationFailureHandler;
import org.yangyi.project.gateway.handler.GatewayAuthenticationSuccessHandler;
import org.yangyi.project.gateway.token.TokenAuthenticationConverter;
import org.yangyi.project.gateway.token.TokenAuthenticationManager;

@Configuration
@EnableWebFluxSecurity
public class GatewayResourceServerConfig extends ResourceServerConfigurerAdapter {

    private final GatewayAuthorizationManager gatewayAuthorizationManager;
    private final WhiteListConfig whiteListConfig;
    private final ServerAccessDeniedHandler serverAccessDeniedHandler;
    private final ServerAuthenticationEntryPoint serverAuthenticationEntryPoint;
    private final ReactiveAuthenticationManager authenticationManager;
    private final ServerAuthenticationConverter serverAuthenticationConverter;
    private final ServerAuthenticationSuccessHandler serverAuthenticationSuccessHandler;
    private final ServerAuthenticationFailureHandler serverAuthenticationFailureHandler;

    public GatewayResourceServerConfig(GatewayAuthorizationManager gatewayAuthorizationManager,
                                       WhiteListConfig whiteListConfig,
                                       GatewayAccessDeniedHandler gatewayAccessDeniedHandler,
                                       GatewayAuthenticationEntryPoint gatewayAuthenticationEntryPoint,
                                       TokenAuthenticationManager tokenAuthenticationManager,
                                       TokenAuthenticationConverter tokenAuthenticationConverter,
                                       GatewayAuthenticationSuccessHandler gatewayAuthenticationSuccessHandler,
                                       GatewayAuthenticationFailureHandler gatewayAuthenticationFailureHandler) {
        this.gatewayAuthorizationManager = gatewayAuthorizationManager;
        this.whiteListConfig = whiteListConfig;
        this.serverAccessDeniedHandler = gatewayAccessDeniedHandler;
        this.serverAuthenticationEntryPoint = gatewayAuthenticationEntryPoint;
        this.authenticationManager = tokenAuthenticationManager;
        this.serverAuthenticationConverter = tokenAuthenticationConverter;
        this.serverAuthenticationSuccessHandler = gatewayAuthenticationSuccessHandler;
        this.serverAuthenticationFailureHandler = gatewayAuthenticationFailureHandler;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity serverHttpSecurity) {
        // 身份认证
        AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(authenticationManager);
        // 登陆验证失败
        authenticationWebFilter.setAuthenticationFailureHandler(serverAuthenticationFailureHandler);
        authenticationWebFilter.setAuthenticationSuccessHandler(serverAuthenticationSuccessHandler);
        // token转换器
        ((TokenAuthenticationConverter) serverAuthenticationConverter).setAllowUriQueryParameter(false);
        authenticationWebFilter.setServerAuthenticationConverter(serverAuthenticationConverter);

        serverHttpSecurity.addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION);

        ServerHttpSecurity.AuthorizeExchangeSpec authorizeExchange = serverHttpSecurity.authorizeExchange();
        authorizeExchange.matchers(EndpointRequest.toAnyEndpoint()).permitAll();
        // 白名单放行
        authorizeExchange.pathMatchers(ArrayUtil.toArray(whiteListConfig.getUrls(), String.class)).permitAll();
        // option 请求默认放行
        authorizeExchange.pathMatchers(HttpMethod.OPTIONS).permitAll();

        authorizeExchange.anyExchange().access(gatewayAuthorizationManager).
                and().exceptionHandling().
                accessDeniedHandler(serverAccessDeniedHandler). // 鉴权失败Handler
                authenticationEntryPoint(serverAuthenticationEntryPoint). // 未认证Handler
                and().headers().frameOptions().disable().
                and().httpBasic().disable().csrf().disable();

        return serverHttpSecurity.build();
    }


}
