package org.yangyi.project.oauth.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.stereotype.Component;

@Component
public class AuthExceptionTranslator implements WebResponseExceptionTranslator<OAuth2Exception> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthExceptionTranslator.class);

    @Override
    public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {
        LOGGER.info("异常信息：", e);
        OAuth2Exception oAuth2Exception = (OAuth2Exception) e;
        return ResponseEntity.status(oAuth2Exception.getHttpErrorCode())
                .body(new AuthException(oAuth2Exception.getMessage()));
    }
}
