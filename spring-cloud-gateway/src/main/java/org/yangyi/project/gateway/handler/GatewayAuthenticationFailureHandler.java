package org.yangyi.project.gateway.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Component
public class GatewayAuthenticationFailureHandler implements ServerAuthenticationFailureHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GatewayAuthenticationFailureHandler.class);

    @Override
    public Mono<Void> onAuthenticationFailure(WebFilterExchange webFilterExchange, AuthenticationException exception) {
        LOGGER.info("ResAuthenticationFailureHandler--->onAuthenticationFailure");
        ServerWebExchange exchange = webFilterExchange.getExchange();
        ServerHttpResponse response = exchange.getResponse();

        //设置headers
        response.getHeaders().setAccessControlAllowCredentials(true);
        response.getHeaders().setAccessControlAllowOrigin("*");
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("code", "401");
        responseMap.put("message", exception.getMessage());

        byte[] dataBytes = {};
        ObjectMapper mapper = new ObjectMapper();
        try {
            dataBytes = mapper.writeValueAsBytes(responseMap);
        } catch (JsonProcessingException e) {
            LOGGER.error("数据转换异常", e);
        }
        DataBuffer bodyDataBuffer = response.bufferFactory().wrap(dataBytes);
        return response.writeWith(Mono.just(bodyDataBuffer)).doOnError((error) -> {
            DataBufferUtils.release(bodyDataBuffer);
        });
    }
}
