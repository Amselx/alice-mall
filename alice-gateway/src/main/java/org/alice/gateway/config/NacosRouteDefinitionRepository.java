package org.alice.gateway.config;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.extern.slf4j.Slf4j;
import org.alice.gateway.common.AliceGatewayProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * The type Nacos route definition repository.
 *
 * @author Amse
 * @version 1.0
 * @date 19 /01/2021 17:33
 */
@Component
@Slf4j
public class NacosRouteDefinitionRepository implements RouteDefinitionRepository {

    @Autowired
    private AliceGatewayProperties gatewayProperties;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private NacosConfigManager nacosConfigManager;

    /**
     * Init.
     */
    @PostConstruct
    public void init() {
        addListener();
    }


    private void addListener() {
        try {
            nacosConfigManager.getConfigService().addListener(
                    gatewayProperties.getRouteDataId(), gatewayProperties.getRouteGroup(),
                    new Listener() {
                @Override
                public Executor getExecutor() {
                    return null;
                }

                @Override
                public void receiveConfigInfo(String configInfo) {
                    log.info("自动更新配置...\r\n" + configInfo);
                    publisher.publishEvent(new RefreshRoutesEvent(this));
                }
            });
        } catch (NacosException e) {
            log.error("nacos-addListener-error", e);
        }
    }



    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        try {
            String content = nacosConfigManager.getConfigService()
                    .getConfig(gatewayProperties.getRouteDataId(), gatewayProperties.getRouteGroup(),5000);
            List<RouteDefinition> routeDefinitions = getListByStr(content);
            return Flux.fromIterable(routeDefinitions);
        } catch (NacosException e) {
            log.error("getRouteDefinitions by nacos error", e);
        }
        return Flux.fromIterable(new ArrayList<>());
    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return null;
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        return null;
    }

    private List<RouteDefinition> getListByStr(String content) {
        if (StringUtils.isNotEmpty(content)) {
            return JSONObject.parseArray(content, RouteDefinition.class);
        }
        return new ArrayList<>(0);
    }
}
