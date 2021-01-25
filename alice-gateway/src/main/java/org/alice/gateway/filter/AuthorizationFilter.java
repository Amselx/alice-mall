package org.alice.gateway.filter;

import org.alice.gateway.common.TokenInfo;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * The type Authorization filter.
 *
 * @author Fox
 */
@Component
@Order(2)
public class AuthorizationFilter extends AbstractAuthFilter {

    @Override
    Mono<Void> authFilter(ServerWebExchange exchange, GatewayFilterChain chain) {
        TokenInfo tokenInfo = exchange.getAttribute("tokenInfo");

        if (tokenInfo == null) {
            throw new RuntimeException("token丢失");
        }

        if (!tokenInfo.isActive()) {
            throw new RuntimeException("token过期");
        }

        String requestPath = exchange.getRequest().getURI().getPath();

        hasPermission(tokenInfo, requestPath);

        return chain.filter(exchange);
    }

    /**
     * has permission.
     *
     * @param tokenInfo info
     * @param currentUrl uri
     */
    private void hasPermission(TokenInfo tokenInfo, String currentUrl) {
        //登录用户的权限集合判断
        String[] permissions = tokenInfo.getAuthorities();
        for (String url : permissions) {
            if (currentUrl.contains(url)) {
                return;
            }
        }

        throw new RuntimeException("没有权限");
    }

}
