package org.alice.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.alice.data.pojo.TokenInfo;
import org.alice.gateway.common.AliceGatewayProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.annotation.Order;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 认证过滤器
 *
 * @author Fox
 */
@Component
@Order(1)
@Slf4j
public class AuthenticationFilter extends AbstractAuthFilter {

    private static final String BEARER = "bearer ";

    private static final String TOKEN = "token";

    private static final String TOKEN_INFO = "tokenInfo";

    @Autowired
    private AliceGatewayProperties gatewayProperties;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    Mono<Void> authFilter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //获取请求头
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

        //请求头为空
        if (StringUtils.isEmpty(authHeader)) {
            throw new RuntimeException("请求头为空");
        }
        TokenInfo tokenInfo = null;
        try {
            //获取token信息
            tokenInfo = getTokenInfo(authHeader);
        } catch (Exception e) {
            throw new RuntimeException("校验令牌异常");
        }
        exchange.getAttributes().put(TOKEN_INFO, tokenInfo);
        return chain.filter(exchange);
    }

    /**
     * get token info.
     *
     * @param authHeader auth header
     * @return token info
     */
    private TokenInfo getTokenInfo(String authHeader) {
        // 获取token的值
        String token = StringUtils.substringAfter(authHeader, BEARER);

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        headers.setBasicAuth(gatewayProperties.getAuthName(), gatewayProperties.getAuthSecret());

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(TOKEN, token);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

        ResponseEntity<TokenInfo> response = restTemplate.exchange(
                gatewayProperties.getAuthCheckTokenUrl(), HttpMethod.POST, entity, TokenInfo.class);

        return response.getBody();
    }
}
