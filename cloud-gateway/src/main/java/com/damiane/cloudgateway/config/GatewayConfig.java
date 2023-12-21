package com.damiane.cloudgateway.config;

import com.damiane.cloudgateway.filters.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class GatewayConfig {

//    @Bean
//    public GlobalFilter globalFilter(JwtAuthenticationFilter jwtAuthenticationFilter) {
//        return (exchange, chain) -> jwtAuthenticationFilter.filter(exchange, chain);
//    }

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(secretKey);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder, JwtAuthenticationFilter jwtAuthenticationFilter) {
        return builder.routes()
                .route("product-service", r -> r.path("/product/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter))
                        .uri("lb://PRODUCT-SERVICE"))
                .route("account-service", r -> r.path("/accounts/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter))
                        .uri("lb://ACCOUNT-SERVICE"))
                // Add more routes as needed
                .build();
    }

}


