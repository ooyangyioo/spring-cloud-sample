package org.yangyi.project.oauth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenGranter;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeTokenGranter;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.implicit.ImplicitTokenGranter;
import org.springframework.security.oauth2.provider.password.ResourceOwnerPasswordTokenGranter;
import org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter;
import org.springframework.security.oauth2.provider.token.*;
import org.yangyi.project.oauth.exception.AuthExceptionTranslator;
import org.yangyi.project.oauth.service.UserDetailsServiceImpl;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * @EnableAuthorizationServer注解开启OAuth2授权服务机制
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    private final AuthenticationManager authenticationManager; // 认证方法入口
    private final UserDetailsServiceImpl userDetailsService;  // 自定义用户验证数据
    private final AuthExceptionTranslator authExceptionTranslator; // 自定义异常处理
    private final DefaultTokenServices tokenServices;
    private final TokenStore tokenStore;
    private final DataSource dataSource;
    private final ClientDetailsService clientDetailsService;

    public AuthorizationServerConfig(AuthenticationManager authenticationManager,
                                     UserDetailsServiceImpl userDetailsService,
                                     TokenStore tokenStore,
                                     DefaultTokenServices tokenServices,
                                     AuthExceptionTranslator authExceptionTranslator,
                                     DataSource dataSource,
                                     ClientDetailsService clientDetailsService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.tokenStore = tokenStore;
        this.tokenServices = tokenServices;
        this.authExceptionTranslator = authExceptionTranslator;
        this.dataSource = dataSource;
        this.clientDetailsService = clientDetailsService;
    }

    /**
     * 用来配置令牌（token）的访问端点和令牌服务(token services)
     *
     * @param endpoints AuthorizationServerEndpointsConfigurer
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager); // 认证管理器
        endpoints.tokenStore(tokenStore); // token存储位置
        endpoints.userDetailsService(userDetailsService); // 用户账号密码认证
        endpoints.allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST); // 允许POST和GET请求
        endpoints.authorizationCodeServices(authorizationCodeServices());
        endpoints.approvalStore(approvalStore());
        endpoints.tokenServices(tokenServices);
        endpoints.exceptionTranslator(authExceptionTranslator); //  自定义异常返回

        endpoints.tokenGranter(tokenGranter(endpoints));

        //  更改TokenEndpoint中默认的url
//        endpoints.pathMapping("/oauth/token", "/oauth2/token");
//        endpoints.pathMapping("/oauth/authorize", "/oauth2/authorize");
//        endpoints.pathMapping("/oauth/confirm_access", "/oauth2/confirm_access");
//        endpoints.pathMapping("/oauth/error", "/oauth2/error");
//        endpoints.pathMapping("/oauth/check_token", "/oauth2/check_token");
    }

    /**
     * 用来配置令牌端点（Token Endpoint）的安全约束
     *
     * @param security AuthorizationServerSecurityConfigurer
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.allowFormAuthenticationForClients()
                // 开启/oauth/check_token验证端口认证权限访问
                .checkTokenAccess("permitAll()")
                // 开启/oauth/token_key验证端口无权限访问
                .tokenKeyAccess("isAuthenticated()");
    }

    /**
     * 用来配置客户端详情服务（ClientDetailsService）
     * 客户端详情信息在这里初始化.
     * 客户端详情信息可以写入内存或者数据库中.
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsService);
    }

    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        return new JdbcAuthorizationCodeServices(dataSource);
    }

    @Bean
    public ApprovalStore approvalStore() {
        return new JdbcApprovalStore(dataSource);
    }

    private TokenGranter tokenGranter(AuthorizationServerEndpointsConfigurer endpoints) {
        List<TokenGranter> tokenGranters = getDefaultTokenGranters(endpoints);
        // 添加自定义授权模式
        //  tokenGranters.add(new WeChatAbstractTokenGranter(endpoints.getTokenServices(), endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory()));
        return new CompositeTokenGranter(tokenGranters);
    }

    /**
     * 默认的授权模式
     */
    private List<TokenGranter> getDefaultTokenGranters(AuthorizationServerEndpointsConfigurer endpoints) {
        List<TokenGranter> tokenGranters = new ArrayList<>();
        // 添加授权码模式：authorization_code
        tokenGranters.add(new AuthorizationCodeTokenGranter(tokenServices, endpoints.getAuthorizationCodeServices(), clientDetailsService, endpoints.getOAuth2RequestFactory()));
        // 添加刷新令牌的模式
        tokenGranters.add(new RefreshTokenGranter(tokenServices, clientDetailsService, endpoints.getOAuth2RequestFactory()));
        // 添加隐式授权模式：implicit
        tokenGranters.add(new ImplicitTokenGranter(tokenServices, clientDetailsService, endpoints.getOAuth2RequestFactory()));
        // 添加客户端模式：client_credentials
        tokenGranters.add(new ClientCredentialsTokenGranter(tokenServices, clientDetailsService, endpoints.getOAuth2RequestFactory()));
        if (authenticationManager != null) {
            // 添加密码模式：password
            tokenGranters.add(new ResourceOwnerPasswordTokenGranter(authenticationManager, tokenServices, clientDetailsService, endpoints.getOAuth2RequestFactory()));
        }
        return tokenGranters;
    }

}
