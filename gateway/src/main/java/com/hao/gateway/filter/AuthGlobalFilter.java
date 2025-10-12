package com.hao.gateway.filter;



import com.hao.common.constant.JwtClaimsConstant;
import com.hao.common.constant.MessageConstant;
import com.hao.common.exception.PermissionException;
import com.hao.common.exception.UserException;
import com.hao.common.properties.JwtProperties;
import com.hao.common.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.stream.Stream;

@Component
@Order(-1) // 最高优先级
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    @Autowired
    private JwtProperties jwtProperties;

    private static final String TOKEN = "token";
    // 路径匹配器（支持Ant风格通配符，如/**、/api/*）
    private final AntPathMatcher matcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String currentPath = request.getURI().getPath();

        // 白名单放行
        if (isWhiteListPath(currentPath)) {
            return chain.filter(exchange);
        }

        // 获取token
        String token = request.getHeaders().getFirst(TOKEN);
        Claims claims;

        try {
            claims = JwtUtil.parseJWT(jwtProperties.getUserSecretKey(), token);
        } catch (RuntimeException e) {
            return writeResponse(response, HttpStatus.UNAUTHORIZED, MessageConstant.LOGIN_EXPIRED);
        }

        // 解析角色
        Integer userRole = claims.get("role", Integer.class);

        // 角色权限校验
        if (!isAccessAllowed(currentPath, userRole)) {
            throw new PermissionException(MessageConstant.NO_PERMISSION);
        }

        Long userId = claims.get("userId", Long.class);

        // 校验通过，放行并传递用户信息
        ServerHttpRequest newRequest = request.mutate()
                .header("X-User-Id", userId.toString())
                .header("X-User-Role", userRole.toString())
                .build();

        return chain.filter(exchange.mutate().request(newRequest).build());
    }

    private boolean isWhiteListPath(String currentPath) {
        return Stream.concat(
                jwtProperties.getPublicPaths().stream(),
                jwtProperties.getAuthPaths().stream()
                ).anyMatch(path -> matcher.match(path, currentPath));
    }

    private boolean isAccessAllowed(String currentPath, Integer role) {
        if (role == 2) {
            return Stream.concat(
                    jwtProperties.getRoleBasedPath().get("teacher").stream(),
                    jwtProperties.getSharedPaths().stream()
                    ).anyMatch(path -> matcher.match(path, currentPath));
        }
        if (role == 1) {
            return Stream.concat(
                    jwtProperties.getRoleBasedPath().get("student").stream(),
                    jwtProperties.getSharedPaths().stream()
                    ).anyMatch(path -> matcher.match(path, currentPath));
        }
        return false;
    }

    private Mono<Void> writeResponse(ServerHttpResponse response, HttpStatus status, String message) {
        response.setStatusCode(status);
        response.getHeaders().add("content-type", "application/json;charset=UTF-8");
        String body = String.format("{\"code\": %d, \"msg\": \"%s\"}", status.value(), message);
        DataBuffer buffer = response.bufferFactory().wrap(body.getBytes());
        return response.writeWith(Mono.just(buffer));
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
