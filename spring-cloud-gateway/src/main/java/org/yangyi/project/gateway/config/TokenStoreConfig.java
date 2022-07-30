package org.yangyi.project.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.sql.DataSource;

@Configuration
public class TokenStoreConfig {

    private final DataSource dataSource;
    private final RedisConnectionFactory redisConnectionFactory;

    public TokenStoreConfig(DataSource dataSource,
                            RedisConnectionFactory redisConnectionFactory) {
        this.dataSource = dataSource;
        this.redisConnectionFactory = redisConnectionFactory;
    }

    @Bean
    @Primary
    public TokenStore jdbcTokenStore() {
        return new JdbcTokenStore(dataSource);
    }

    @Bean
    public TokenStore redisTokenStore() {
        return new RedisTokenStore(redisConnectionFactory);
    }

//    @Bean
//    public TokenStore jwtTokenStore() {
//        JwtTokenStore jwtTokenStore = new JwtTokenStore(jwtAccessTokenConverter());
//        return jwtTokenStore;
//    }

}
