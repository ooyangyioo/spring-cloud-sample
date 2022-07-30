package org.yangyi.project.gateway.token;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.BearerTokenError;
import org.springframework.security.oauth2.server.resource.BearerTokenErrorCodes;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * 验证Token
 */
@Component
public class TokenAuthenticationManager implements ReactiveAuthenticationManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenAuthenticationManager.class);

    private final TokenStore tokenStore;

    public TokenAuthenticationManager(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        LOGGER.info("TokenAuthenticationManager--->authenticate");
        return Mono.justOrEmpty(authentication)
                .filter(a -> a instanceof BearerTokenAuthenticationToken)
                .cast(BearerTokenAuthenticationToken.class)
                .map(BearerTokenAuthenticationToken::getToken)
                .flatMap((accessTokenValue -> {
                    OAuth2AccessToken accessToken = tokenStore.readAccessToken(accessTokenValue);
                    if (accessToken == null) {
                        OAuth2Error error = new BearerTokenError(
                                BearerTokenErrorCodes.INVALID_TOKEN,
                                HttpStatus.UNAUTHORIZED,
                                "Invalid Access Token",
                                null);
                        return Mono.error(new OAuth2AuthenticationException(error));
                    } else if (accessToken.isExpired()) {
                        tokenStore.removeAccessToken(accessToken);
                        OAuth2Error error = new BearerTokenError(
                                BearerTokenErrorCodes.INVALID_TOKEN,
                                HttpStatus.UNAUTHORIZED,
                                "Access Token Expired",
                                null);
                        return Mono.error(new OAuth2AuthenticationException(error));
                    }

                    OAuth2Authentication result = tokenStore.readAuthentication(accessToken);
                    if (result == null) {
                        OAuth2Error error = new BearerTokenError(
                                BearerTokenErrorCodes.INVALID_TOKEN,
                                HttpStatus.UNAUTHORIZED,
                                "Invalid Access Token",
                                null);
                        return Mono.error(new OAuth2AuthenticationException(error));
                    }
                    return Mono.just(result);
                })).cast(Authentication.class);
    }
}
