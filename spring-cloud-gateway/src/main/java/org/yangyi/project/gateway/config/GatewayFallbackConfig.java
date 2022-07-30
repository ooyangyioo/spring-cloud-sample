package org.yangyi.project.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.yangyi.project.gateway.handler.GatewayFallbackHandler;

@Configuration
public class GatewayFallbackConfig {

    private final GatewayFallbackHandler gatewayFallbackHandler;

    public GatewayFallbackConfig(GatewayFallbackHandler gatewayFallbackHandler) {
        this.gatewayFallbackHandler = gatewayFallbackHandler;
    }

    @Bean
    public RouterFunction routerFunction() {
        return RouterFunctions.route(
                RequestPredicates.GET("/fallback")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), gatewayFallbackHandler);
    }

}
