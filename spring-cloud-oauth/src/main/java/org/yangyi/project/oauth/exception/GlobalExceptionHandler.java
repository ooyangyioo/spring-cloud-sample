package org.yangyi.project.oauth.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 其余异常
     *
     * @param throwable 异常信息
     * @return 封装异常信息
     */
    @ExceptionHandler(Throwable.class)
    public Map<String, Object> exceptionHandler(Throwable throwable, HttpServletRequest request, HttpServletResponse response) {
        LOGGER.error(String.format("错误请求地址: %s, 请求来源: %s", request.getContextPath(), request.getRemoteAddr()), throwable);
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new HashMap<String, Object>() {{
            put("code", "500");
            put("message", "服务器出错了");
        }};
    }

    /**
     * 处理404异常
     *
     * @param throwable 异常信息
     * @return 封装异常信息
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public Map<String, Object> noHandlerFound(Throwable throwable, HttpServletRequest request, HttpServletResponse response) {
        LOGGER.error(String.format("错误请求地址: %s, 请求来源: %s", request.getContextPath(), request.getRemoteAddr()), throwable);
        response.setStatus(HttpStatus.NOT_FOUND.value());
        return new HashMap<String, Object>() {{
            put("code", "404");
            put("message", "访问地址不存在");
        }};
    }
}
