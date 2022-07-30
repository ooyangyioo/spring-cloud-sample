package org.yangyi.project.oauth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.yangyi.project.oauth.service.InMemoryClientDetailsServiceImpl;

import javax.sql.DataSource;

/**
 * 加载客户端信息
 */
@Configuration
public class ClientDetailsServiceConfig {

    private final DataSource dataSource;

    @Autowired
    public ClientDetailsServiceConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * 从数据库加载
     *
     * @return ClientDetailsService
     */
    @Bean("jdbcClientDetailsService")
    public ClientDetailsService jdbcClientDetailsService() {
        return new JdbcClientDetailsService(dataSource);
    }

    /**
     * 从内存中加载
     *
     * @return ClientDetailsService
     */
    @Bean("inMemoryClientDetailsService")
    @Primary
    public ClientDetailsService inMemoryClientDetailsService() {
        return new InMemoryClientDetailsServiceImpl();
    }

}
