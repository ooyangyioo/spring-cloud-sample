package org.yangyi.project.gateway.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Component
public class GatewayFallbackHandler implements HandlerFunction<ServerResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GatewayFallbackHandler.class);

    @Override
    public Mono<ServerResponse> handle(ServerRequest serverRequest) {
        serverRequest.attribute(ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR)
                .ifPresent(originalUrls -> LOGGER.error("网关执行请求:{}失败,hystrix服务降级处理", originalUrls));
        Map<String, String> resp = new HashMap<>();
        resp.put("code", "99");
        resp.put("message", "程序调用失败");
        return ServerResponse
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(resp));
    }

}
