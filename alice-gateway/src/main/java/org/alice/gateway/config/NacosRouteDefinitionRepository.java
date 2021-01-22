package org.alice.gateway.config;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
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
 *
 *
 * @author  Amse
 * @date  19/01/2021 17:33
 * @version 1.0
 */
@ConfigurationProperties("gateway.dynamic-route")
@Component
@Slf4j
public class NacosRouteDefinitionRepository implements RouteDefinitionRepository {

    @Setter
    private String dataId = null;

    @Setter
    private String group = null;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private NacosConfigManager nacosConfigManager;

    @PostConstruct
    public void init() {
        if (dataId != null) {
            addListener();
        }
    }


    private void addListener() {
        try {
            nacosConfigManager.getConfigService().addListener(dataId, group, new Listener() {
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
                    .getConfig(dataId, group,5000);
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
