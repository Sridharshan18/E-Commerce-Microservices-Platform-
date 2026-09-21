package com.ecommerce.gateway;


import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("product-service" , r -> r
                        .path("/api/products/**")
                        .filters(f -> f.retry(config -> config
                                .setRetries(5)
                                .setMethods(HttpMethod.GET))
                        .circuitBreaker(config -> config
                                .setName("ecomBreaker")
                                .setFallbackUri("/fallback/products")))
//                        .filters(f -> f.rewritePath("/products(?<segment>/?.*)",
//                                "/api/products${segment}"))
                        .uri("lb://PRODUCT-SERVICE"))
                .route("user-service" , r -> r
                        .path("/api/users/**")
//                        .filters(f -> f.rewritePath("/users(?<segment>/?.*)",
//                                "/api/users${segment}"))
                        .uri("lb://USER-SERVICE"))
                .route("order-service" , r -> r
                        .path("/api/orders/**", "/api/cart/**")
//                        .filters(f -> f.rewritePath("/(?<segment>/?.*)",
//                                "/api/${segment}"))
                        .uri("lb://ORDER-SERVICE"))
                .route("eureka" , r -> r
                        .path("/eureka/main")
                        .filters(f -> f.rewritePath("/eureka/main", "/"))
                        .uri("http://localhost:8761"))
                .route("eureka-server-static" , r -> r
                        .path("/eureka/**")
                        .uri("http://localhost:8761"))
                .build();
    }


}
