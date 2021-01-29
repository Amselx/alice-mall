package org.alice.gateway.common;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

/**
 * The type Skip authentication nacos manage.
 *
 * @author Amse
 * @version 1.0.0
 * @date 25 /01/2021 15:57
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
    public void afterPropertiesSet() {
        updateSkipUrl();
        shouldSkipUrlListener();

    }

    private void updateSkipUrl() {
        String config;
        try {
            config = nacosConfigManager.getConfigService().getConfig(gatewayProperties.getSkipUriDataId(), gatewayProperties.getSkipUriGroup(), 200);
            skipUrlHandler(config);
        } catch (NacosException e) {
            log.error("updateSkipUrl-error", e);
        }
    }

    private void skipUrlHandler(String config) {
        if (StringUtils.isBlank(config)) {
            return;
        }
        String[] split = config.split(",");
        Set<String> uri = Arrays.stream(split).map(StringUtils::trim).collect(Collectors.toSet());
        log.info("自动更新URI配置...\r\n" + uri);
        shouldSkipUrl = uri;
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
                    skipUrlHandler(configInfo);
                }
            });
        } catch (NacosException e) {
            log.error("nacos-addListener-shouldSkipUrlListener-error", e);
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
