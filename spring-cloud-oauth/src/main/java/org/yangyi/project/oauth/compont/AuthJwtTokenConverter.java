package org.yangyi.project.oauth.compont;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.LinkedHashMap;
import java.util.Map;

public class AuthJwtTokenConverter extends JwtAccessTokenConverter {

    private static final String CLIENT_CREDENTIALS = "client_credentials";

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        if (CLIENT_CREDENTIALS.equals(authentication.getOAuth2Request().getGrantType())) {
            return super.enhance(accessToken, authentication);
        }

        Object principal = authentication.getUserAuthentication().getPrincipal();
        Map<String, Object> additionalInformation = new LinkedHashMap<>(8);
        additionalInformation.put("uuid", "abcd123456");
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInformation);
        return super.enhance(accessToken, authentication);
    }
}
