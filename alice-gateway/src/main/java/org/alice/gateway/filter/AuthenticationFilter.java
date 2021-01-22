package org.alice.gateway.filter;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.alice.gateway.common.MDA;
import org.alice.gateway.common.TokenInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.Executor;

/**
 * 认证过滤器
 * @author Fox
 */
@ConfigurationProperties("gateway.skip-uri")
@Component
@Order(1)
@Slf4j
public class AuthenticationFilter implements GlobalFilter, InitializingBean {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private NacosConfigManager nacosConfigManager;

    private static Set<String> shouldSkipUrl;

    @Setter
    private String dataId;

    @Setter
    private String group;

    @Override
    public void afterPropertiesSet() throws Exception {
//        nacosConfigManager.getConfigService().getConfig()
        // 不拦截认证的请求
//        shouldSkipUrl.add("/oauth/token");
//        shouldSkipUrl.add("/oauth/check_token");
//        shouldSkipUrl.add("/user/getCurrentUser");
        this.shouldSkipUrlListener();
    }


    private void shouldSkipUrlListener() {
        try {
            nacosConfigManager.getConfigService().addListener(dataId, group, new Listener() {
                @Override
                public Executor getExecutor() {
                    return null;
                }

                @Override
                public void receiveConfigInfo(String configInfo) {
                    log.info("自动更新配置...\r\n" + configInfo);
                    shouldSkipUrl = Sets.newHashSet(configInfo.split(","));
                }
            });
        } catch (NacosException e) {
            log.error("nacos-addListener-error", e);
        }
    }


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String requestPath = exchange.getRequest().getURI().getPath();

        //不需要认证的url
        if(shouldSkip(requestPath)) {
            return chain.filter(exchange);
        }
        //获取请求头
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

        //请求头为空
        if(StringUtils.isEmpty(authHeader)) {
            throw new RuntimeException("请求头为空");
        }

        TokenInfo tokenInfo=null;
        try {
            //获取token信息
            tokenInfo = getTokenInfo(authHeader);
        }catch (Exception e) {
            throw new RuntimeException("校验令牌异常");
        }
        exchange.getAttributes().put("tokenInfo",tokenInfo);
        return chain.filter(exchange);
    }

    private boolean shouldSkip(String reqPath) {
        try {
            String config = nacosConfigManager.getConfigService().getConfig(group, dataId, 3L);

        } catch (NacosException e) {

        }

        for(String skipPath:shouldSkipUrl) {
            if(reqPath.contains(skipPath)) {
                return true;
            }
        }
        return false;
    }

    private TokenInfo getTokenInfo(String authHeader) {
        // 获取token的值
        String token = StringUtils.substringAfter(authHeader, "bearer ");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(MDA.CLIENT_ID, MDA.CLIENT_SECRET);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("token", token);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

        ResponseEntity<TokenInfo> response = restTemplate.exchange(MDA.CHECK_TOKEN_URL, HttpMethod.POST, entity, TokenInfo.class);

        return response.getBody();
    }
}
