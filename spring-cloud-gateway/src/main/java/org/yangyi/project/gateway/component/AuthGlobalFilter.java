package org.yangyi.project.gateway.component;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthGlobalFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (StringUtils.isNotEmpty(token)) {
            String accessToken = token.replace("Bearer ", "");
            LOGGER.info("Access Token :{}", accessToken);
            ServerHttpRequest request = exchange
                    .getRequest()
                    .mutate()
                    .header("user", accessToken)
                    .build();
            exchange = exchange.mutate().request(request).build();
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
