package org.alice.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The type Gateway service application.
 *
 * @author Amse
 * @version 1.0.0
 * @date 18 /01/2021 18:03
 */
@SpringBootApplication
public class GatewayServiceApplication {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(GatewayServiceApplication.class, args);
    }
}
