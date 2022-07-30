package org.yangyi.project.oauth.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;

/**
 * 自定义客户端数据
 * 从内存中获取
 */
public class InMemoryClientDetailsServiceImpl implements ClientDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InMemoryClientDetailsServiceImpl.class);

    private ClientDetailsService clientDetailsService;

    /**
     * 初始化client数据
     *
     * @PostConstruct修饰的方法会在服务器加载Servlet的时候运行，并且只会被服务器调用一次，类似于Serclet的inti()方法.
     * @PostConstruct修饰的方法会在构造函数之后，init()方法之前运行.
     */
    @PostConstruct
    public void initClients() {
        LOGGER.info("开始加载客户端数据");
        InMemoryClientDetailsServiceBuilder clientDetailsServiceBuilder = new InMemoryClientDetailsServiceBuilder();
        clientDetailsServiceBuilder
                .withClient("web")
                .secret("{bcrypt}$2a$10$KQmlTr0m3vaXUjWTaX1qiuP3Y4FbXwyJ44bZ5d1rG4z83RRHRlG3W")
                .authorizedGrantTypes("authorization_code", "password", "implicit", "client_credentials", "refresh_token")
                .redirectUris("https://www.baidu.com")
                .autoApprove(false)
                .scopes("read", "write")
                .resourceIds("user");
        try {
            clientDetailsService = clientDetailsServiceBuilder.build();
        } catch (Exception e) {
            LOGGER.error("加载客户端数据异常", e);
        }
    }

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        if (StringUtils.isEmpty(clientId))
            throw new ClientRegistrationException("客户端不存在");
        return clientDetailsService.loadClientByClientId(clientId);
    }
}
