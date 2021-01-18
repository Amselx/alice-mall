package org.alice.api;

import com.alice.entity.UmsAccount;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * The interface Ums service api.
 *
 * @author Amse
 * @version 1.0.0
 * @date 18 /01/2021 11:00
 */
@FeignClient("mall-ums")
public interface UmsServiceApi {
    /**
     * Gets account.
     *
     * @param username the username
     * @return account
     */
    @GetMapping("/user/getAccount/{username}")
    public UmsAccount getAccount(@PathVariable("username") String username);
}
