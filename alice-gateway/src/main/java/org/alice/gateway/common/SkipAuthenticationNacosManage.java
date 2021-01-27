package org.alice.gateway.common;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executor;

/**
 * @author Amse
 * @version 1.0.0
 * @date 25/01/2021 15:57
 */
@Slf4j
@Component
public class SkipAuthenticationNacosManage implements InitializingBean {

    @Autowired
    private AliceGatewayProperties gatewayProperties;

    @Autowired
    private NacosConfigManager nacosConfigManager;

    private static Set<String> shouldSkipUrl = new HashSet<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        shouldSkipUrlListener();
    }

    private void shouldSkipUrlListener() {
        try {
            nacosConfigManager.getConfigService().addListener(
                    gatewayProperties.getSkipUriDataId(), gatewayProperties.getSkipUriGroup(),
                    new Listener() {
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

    /**
     * Should skip uri boolean.
     *
     * @param uri the uri
     * @return the boolean
     */
    public static boolean shouldSkipUri(String uri) {
        return shouldSkipUrl.contains(uri);
    }

}
