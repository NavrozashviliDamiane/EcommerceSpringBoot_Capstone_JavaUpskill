package com.damiane.cloudgateway.filters;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@Component
public class JwtAuthenticationFilter implements GatewayFilter, Ordered {

    private static final String AUTH_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final List<String> ALLOWED_PATHS = Arrays.asList("/accounts/sign-in", "/accounts/create", "/product/all", "/product/by-id/**",
            "/product/search-sort/**");
    private final byte[] secretKey;

    public JwtAuthenticationFilter(@Value("${jwt.secret-key}") String encodedSecretKey) {
        this.secretKey = decodeSecretKey(encodedSecretKey);
    }

    private byte[] decodeSecretKey(String encodedKey) {
        try {
            return Base64.getDecoder().decode(encodedKey);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid base64 encoded secret key", e);
        }
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        String path = request.getURI().getPath();

        // Log the incoming path
        System.out.println("Incoming Path: " + path);

        // Log the incoming headers
        request.getHeaders().forEach((name, values) -> {
            System.out.println("Header: " + name + " = " + values);
        });

        if (!requiresAuthentication(path)) {
            return chain.filter(exchange);
        }

        if (isTokenMissing(request)) {
            System.out.println("Token is missing for path: " + path);
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        String token = extractToken(request);

        try {
            System.out.println("Extracted Token: " + token);
            System.out.println("Secret Key Used: " + secretKey);
            Claims claims = Jwts.parser().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
            List<String> roles = (List<String>) claims.get("roles");
            System.out.println("Token Claims: " + claims.toString());
            System.out.println("Secret key fetched successfully.");

            for (String role : roles) {
                if (!isValidAccess(String.valueOf(request.getMethod()), role)) {
                    System.out.println("Access denied for path: " + path + ", method: " + request.getMethod());
                    response.setStatusCode(HttpStatus.FORBIDDEN);
                    return response.setComplete();
                }
            }

            return chain.filter(exchange);
        } catch (ExpiredJwtException ex) {
            System.out.println("Token expired for path: " + path);
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        } catch (Exception e) {
            System.out.println("Exception occurred: " + e.getMessage());
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
    }

    private boolean requiresAuthentication(String path) {
        return !ALLOWED_PATHS.contains(path);
    }

    public boolean isTokenMissing(ServerHttpRequest request) {
        String authHeader = request.getHeaders().getFirst(AUTH_HEADER);
        return (authHeader == null || !authHeader.startsWith(TOKEN_PREFIX));
    }

    public String extractToken(ServerHttpRequest request) {
        String authHeader = request.getHeaders().getFirst(AUTH_HEADER);
        return authHeader.replace(TOKEN_PREFIX, "");
    }

    public boolean isValidAccess(String method, String role) {
        if (role.equals("ADMIN")) {
            return true;
        } else return role.equals("CUSTOMER") && method.equals("GET");
    }

    @Override
    public int getOrder() {
        return -1; // Set the filter order
    }
}
