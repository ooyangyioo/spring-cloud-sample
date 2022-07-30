package org.yangyi.project.oauth.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.*;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.yangyi.project.oauth.compont.AuthJwtTokenConverter;

import javax.sql.DataSource;
import java.security.KeyPair;

/**
 * Token持久化
 */
@Configuration
public class TokenStoreConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenStoreConfig.class);

    private final DataSource dataSource;
    private final RedisConnectionFactory redisConnectionFactory;
    private final KeyStoreConfig keyStoreConfig;

    public TokenStoreConfig(DataSource dataSource,
                            RedisConnectionFactory redisConnectionFactory,
                            KeyStoreConfig keyStoreConfig) {
        this.dataSource = dataSource;
        this.redisConnectionFactory = redisConnectionFactory;
        this.keyStoreConfig = keyStoreConfig;
    }

    /**
     * JDBC模式
     *
     * @return
     */
    @Bean(name = "jdbcTokenStore")
    @Primary
    public TokenStore jdbcTokenStore() {
        return new JdbcTokenStore(dataSource);
    }

    /**
     * Redis模式
     *
     * @return
     */
    @Bean(name = "redisTokenStore")
    public TokenStore redisTokenStore() {
        return new RedisTokenStore(redisConnectionFactory);
    }

    /**
     * 内存模式
     *
     * @return
     */
    @Bean(name = "inMemoryTokenStore")
    public TokenStore inMemoryTokenStore() {
        return new InMemoryTokenStore();
    }

    /**
     * JWT 令牌存储方案
     *
     * @return
     */
    @Bean(name = "customJwtTokenStore")
    public TokenStore customJwtTokenStore() {
        JwtTokenStore jwtTokenStore = new JwtTokenStore(jwtAccessTokenConverter());
        return jwtTokenStore;
    }

    /**
     * 使用非对称加密算法对token签名
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new AuthJwtTokenConverter();
        converter.setKeyPair(keyPair());
        return converter;
    }

    /**
     * 从classpath下的密钥库中获取密钥对(公钥+私钥)
     */
    @Bean
    public KeyPair keyPair() {
        KeyStoreKeyFactory factory = new KeyStoreKeyFactory(
                new ClassPathResource(keyStoreConfig.getName()),
                keyStoreConfig.getPassword().toCharArray()
        );

        return factory.getKeyPair(
                keyStoreConfig.getAlias(),
                keyStoreConfig.getPassword().toCharArray()
        );
    }

}
