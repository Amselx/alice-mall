package org.alice.gateway.filter;

import org.alice.gateway.common.TokenInfo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Fox
 */
@Component
@Order(1)
public class AuthorizationFilter implements GlobalFilter, InitializingBean {


    private static Set<String> shouldSkipUrl = new LinkedHashSet<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        // 不拦截认证的请求
        shouldSkipUrl.add("/oauth/token");
        shouldSkipUrl.add("/oauth/check_token");
        shouldSkipUrl.add("/user/getCurrentUser");

    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String requestPath = exchange.getRequest().getURI().getPath();
        //不需要认证的url
        if (shouldSkip(requestPath)) {
            return chain.filter(exchange);
        }
        TokenInfo tokenInfo = exchange.getAttribute("tokenInfo");

        assert tokenInfo != null;
        if (!tokenInfo.isActive()) {
            throw new RuntimeException("token过期");
        }

        hasPermission(tokenInfo, requestPath);

        return chain.filter(exchange);
    }

    private boolean shouldSkip(String reqPath) {
        for (String skipPath : shouldSkipUrl) {
            if (reqPath.contains(skipPath)) {
                return true;
            }
        }
        return false;
    }

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
