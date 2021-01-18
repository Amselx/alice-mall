package org.alice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author Amse
 * @date 12/01/2021 16:39
 * @version 1.0.0
 */
@SpringBootApplication
//@MapperScan("org.alice.*.mapper")
@EnableFeignClients
public class AuthorizationApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthorizationApplication.class, args);
    }
}
