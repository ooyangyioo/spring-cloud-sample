package org.yangyi.project.oauth.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

/**
 * 异常处理类
 */
@JsonSerialize(using = AuthExceptionSerializer.class)
public class AuthException extends OAuth2Exception {

    public AuthException(String msg, Throwable t) {
        super(msg, t);
    }

    public AuthException(String msg) {
        super(msg);
    }
}
