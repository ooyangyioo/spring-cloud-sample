package org.yangyi.project.gateway.token;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.BearerTokenError;
import org.springframework.security.oauth2.server.resource.BearerTokenErrorCodes;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class TokenAuthenticationConverter implements ServerAuthenticationConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenAuthenticationConverter.class);

    private static final Pattern authorizationPattern = Pattern.compile("^Bearer (?<token>[a-zA-Z0-9-._~+/]+)=*$");
    private boolean allowUriQueryParameter = false;

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        LOGGER.info("TokenAuthenticationConverter--->convert");
        return Mono.justOrEmpty(this.token(exchange.getRequest()))
                .map(BearerTokenAuthenticationToken::new);
    }


    private String token(ServerHttpRequest request) {
        String authorizationHeaderToken = resolveFromAuthorizationHeader(request.getHeaders());
        String parameterToken = request.getQueryParams().getFirst("access_token");
        if (authorizationHeaderToken != null) {
            // must have value {"code":401,"msg":"Not Authenticated"}
            if (parameterToken != null) {
                //add by someday
                if (!authorizationHeaderToken.equals(parameterToken)) {
                    BearerTokenError error = new BearerTokenError(BearerTokenErrorCodes.INVALID_REQUEST,
                            HttpStatus.BAD_REQUEST,
                            "Found multiple bearer tokens in the request",
                            null);
                    throw new OAuth2AuthenticationException(error);
                }
            }
            return authorizationHeaderToken;
        }
        //请求参数支持
        else if (parameterToken != null && isParameterTokenSupportedForRequest(request)) {
            return parameterToken;
        }
        return null;
    }

    private static String resolveFromAuthorizationHeader(HttpHeaders headers) {
        String authorization = headers.getFirst(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(authorization) && authorization.startsWith("Bearer")) {
            Matcher matcher = authorizationPattern.matcher(authorization);
            if (!matcher.matches()) {
                BearerTokenError error = new BearerTokenError(BearerTokenErrorCodes.INVALID_TOKEN,
                        HttpStatus.BAD_REQUEST,
                        "Bearer Token is Malformed",
                        null);
                throw new OAuth2AuthenticationException(error);
            }
            return matcher.group("token");
        }
        return null;
    }

    public void setAllowUriQueryParameter(boolean allowUriQueryParameter) {
        this.allowUriQueryParameter = allowUriQueryParameter;
    }

    private boolean isParameterTokenSupportedForRequest(ServerHttpRequest request) {
        return this.allowUriQueryParameter && HttpMethod.GET.equals(request.getMethod());
    }

}
