package org.yangyi.project.oauth.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * 自定义客户端数据
 * 从数据库获取
 */
@Service("customJdbcClientDetailsService")
public class JdbcClientDetailsServiceImpl implements ClientDetailsService {

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {

        // 从数据库查询
        ClientDetails clientDetails = new ClientDetails() {
            @Override
            public String getClientId() {
                return null;
            }

            @Override
            public Set<String> getResourceIds() {
                return null;
            }

            @Override
            public boolean isSecretRequired() {
                return false;
            }

            @Override
            public String getClientSecret() {
                return null;
            }

            @Override
            public boolean isScoped() {
                return false;
            }

            @Override
            public Set<String> getScope() {
                return null;
            }

            @Override
            public Set<String> getAuthorizedGrantTypes() {
                return null;
            }

            @Override
            public Set<String> getRegisteredRedirectUri() {
                return null;
            }

            @Override
            public Collection<GrantedAuthority> getAuthorities() {
                return null;
            }

            @Override
            public Integer getAccessTokenValiditySeconds() {
                return null;
            }

            @Override
            public Integer getRefreshTokenValiditySeconds() {
                return null;
            }

            @Override
            public boolean isAutoApprove(String scope) {
                return false;
            }

            @Override
            public Map<String, Object> getAdditionalInformation() {
                return null;
            }
        };

        return clientDetails;
    }

}
