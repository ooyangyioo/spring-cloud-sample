package org.yangyi.project.gateway.config;//package org.yangyi.project.gateway.config;
//
//import org.springframework.cloud.client.discovery.DiscoveryClient;
//import org.springframework.cloud.gateway.discovery.DiscoveryClientRouteDefinitionLocator;
//import org.springframework.cloud.gateway.discovery.DiscoveryLocatorProperties;
//import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.reactive.CorsWebFilter;
//import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
//import org.springframework.web.util.pattern.PathPatternParser;
//
//@Configuration
//public class CorsConfig {
//
//    @Bean
//    @Order(Ordered.HIGHEST_PRECEDENCE)
//    public CorsWebFilter corsFilter() {
//        CorsConfiguration config = new CorsConfiguration();
//        // cookie跨域
//        config.setAllowCredentials(Boolean.TRUE);
//        config.addAllowedMethod("*");
//        config.addAllowedOrigin("*");
//        config.addAllowedHeader("*");
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());
//        source.registerCorsConfiguration("/**", config);
//        return new CorsWebFilter(source);
//    }
//
//}
