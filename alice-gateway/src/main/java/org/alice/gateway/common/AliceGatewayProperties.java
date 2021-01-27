package org.alice.gateway.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @author Amse
 * @version 1.0.0
 * @date 25/01/2021 16:59
 */
@ConfigurationProperties("gateway")
@Component
@Data
public class AliceGatewayProperties {

    private String routeDataId;

    private String routeGroup;

    private String skipUriDataId;

    private String skipUriGroup;

    private String authName;

    private String authSecret;

    private String authCheckTokenUrl;

}
