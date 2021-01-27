package org.alice.gateway.filter;

import org.alice.gateway.common.SkipAuthenticationNacosManage;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * The type Abstract auth filter.
 *
 * @author Amse
 * @version 1.0.0
 * @date 25 /01/2021 15:49
 */
public abstract class AbstractAuthFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String requestPath = exchange.getRequest().getURI().getPath();

        if (SkipAuthenticationNacosManage.shouldSkipUri(requestPath)) {
            return chain.filter(exchange);
        }

        return authFilter(exchange, chain);
    }

    /**
     * Auth filter mono.
     *
     * @param exchange the exchange
     * @param chain    the chain
     * @return the mono
     */
    abstract Mono<Void> authFilter(ServerWebExchange exchange, GatewayFilterChain chain);

}
