package org.yangyi.project.gateway.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * 自定义鉴权失败时的处理逻辑
 */
@Component
public class GatewayAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(GatewayAuthenticationEntryPoint.class);

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException e) {
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().setAccessControlAllowCredentials(true);
        response.getHeaders().setAccessControlAllowOrigin("*");
        response.getHeaders().setCacheControl(CacheControl.noCache());
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        response.setStatusCode(HttpStatus.UNAUTHORIZED);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("code", "-1");
        responseMap.put("message", "未认证");
        byte[] message = null;
        try {
            message = new ObjectMapper().writeValueAsBytes(responseMap);
        } catch (JsonProcessingException exception) {
            LOGGER.error("数据格式化异常", exception);
        }
        DataBufferFactory dataBufferFactory = response.bufferFactory();
        DataBuffer buffer = dataBufferFactory.wrap(message);
        return response.writeWith(Mono.just(buffer)).doOnError((error) -> DataBufferUtils.release(buffer));
    }
}
