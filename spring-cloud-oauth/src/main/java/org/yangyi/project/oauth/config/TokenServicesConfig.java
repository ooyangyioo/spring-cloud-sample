package org.yangyi.project.oauth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class TokenServicesConfig {

    private static final String CLIENT_CREDENTIALS = "client_credentials";

    private final JwtAccessTokenConverter jwtAccessTokenConverter;
    private final TokenStore tokenStore;
    private final ClientDetailsService clientDetailsService;

    public TokenServicesConfig(JwtAccessTokenConverter jwtAccessTokenConverter,
                               TokenStore tokenStore,
                               ClientDetailsService clientDetailsService) {
        this.jwtAccessTokenConverter = jwtAccessTokenConverter;
        this.tokenStore = tokenStore;
        this.clientDetailsService = clientDetailsService;
    }

    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        //  Token持久化容器
        tokenServices.setTokenStore(tokenStore);
        //  客户端信息
        tokenServices.setClientDetailsService(clientDetailsService);
        //  Token增强
        tokenServices.setTokenEnhancer(tokenEnhancer());
        //  access_token 的有效时长 (秒), 默认 12 小时 在数据库设置
        //  tokenServices.setAccessTokenValiditySeconds(60 * 60 * 12);
        //  refresh_token 的有效时长 (秒), 默认 30 天  在数据库设置
        //  tokenServices.setRefreshTokenValiditySeconds(60 * 60 * 24 * 7);
        //  是否支持refresh_token，默认false
        tokenServices.setSupportRefreshToken(true);
        //  是否复用refresh_token,默认为true(如果为false,则每次请求刷新都会删除旧的refresh_token,创建新的refresh_token)
//        tokenServices.setReuseRefreshToken(false);
        return tokenServices;
    }

    @Bean
    public DefaultTokenServices jwtTokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        //  Token持久化容器
        tokenServices.setTokenStore(tokenStore);
        //  客户端信息
        tokenServices.setClientDetailsService(clientDetailsService);

        TokenEnhancerChain chain = new TokenEnhancerChain();
        chain.setTokenEnhancers(Arrays.asList(jwtAccessTokenConverter, tokenEnhancer()));
        tokenServices.setTokenEnhancer(chain);
        //  access_token 的有效时长 (秒), 默认 12 小时 在数据库设置
        //  tokenServices.setAccessTokenValiditySeconds(60 * 60 * 12);
        //  refresh_token 的有效时长 (秒), 默认 30 天  在数据库设置
        //  tokenServices.setRefreshTokenValiditySeconds(60 * 60 * 24 * 7);
        //  是否支持refresh_token，默认false
        tokenServices.setSupportRefreshToken(true);
        //  是否复用refresh_token,默认为true(如果为false,则每次请求刷新都会删除旧的refresh_token,创建新的refresh_token)
//        tokenServices.setReuseRefreshToken(false);
        return tokenServices;
    }

    /**
     * Token增强
     * 客户端模式不增强
     *
     * @return
     */
    @Bean
    public TokenEnhancer tokenEnhancer() {
        return (accessToken, authentication) -> {
            if (CLIENT_CREDENTIALS.equals(authentication.getOAuth2Request().getGrantType())) {
                return accessToken;
            }
            Map<String, Object> additionalInformation = new HashMap<>(8);
            Object principal = authentication.getUserAuthentication().getPrincipal();
            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInformation);
            return accessToken;
        };
    }

}
