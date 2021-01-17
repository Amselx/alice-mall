package org.alice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Amse
 * @date 12/01/2021 16:39
 * @version 1.0.0
 */
@SpringBootApplication
//@MapperScan("org.alice.*.mapper")
public class AuthorizationApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthorizationApplication.class, args);
    }
}
