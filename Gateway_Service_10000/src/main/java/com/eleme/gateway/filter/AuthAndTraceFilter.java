package com.eleme.gateway.filter;

import java.util.Set;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthAndTraceFilter implements GlobalFilter, Ordered {
    private static final Set<String> PUBLIC_PATH_PREFIXES = Set.of(
            "/actuator",
            "/api/users/login",
            "/api/users/register",
            "/api/categories",
            "/api/merchants"
    );

    @Value("${gateway.auth.enabled:true}")
    private boolean authEnabled;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, org.springframework.cloud.gateway.filter.GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        ServerHttpRequest.Builder mutated = request.mutate();

        String requestId = request.getHeaders().getFirst("X-Request-Id");
        if (!StringUtils.hasText(requestId)) {
            mutated.header("X-Request-Id", UUID.randomUUID().toString());
        }

        String authorization = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        String xToken = request.getHeaders().getFirst("X-Token");
        if (!StringUtils.hasText(xToken) && StringUtils.hasText(authorization)) {
            if (authorization.startsWith("Bearer ")) {
                mutated.header("X-Token", authorization.substring("Bearer ".length()).trim());
            } else {
                mutated.header("X-Token", authorization.trim());
            }
        }

        if (authEnabled && isProtectedPath(path)) {
            String token = mutated.build().getHeaders().getFirst("X-Token");
            if (!StringUtils.hasText(token)) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
        }

        return chain.filter(exchange.mutate().request(mutated.build()).build());
    }

    private boolean isProtectedPath(String path) {
        for (String prefix : PUBLIC_PATH_PREFIXES) {
            if (path.startsWith(prefix)) {
                return false;
            }
        }
        return path.startsWith("/api/");
    }

    @Override
    public int getOrder() {
        return -100;
    }
}
