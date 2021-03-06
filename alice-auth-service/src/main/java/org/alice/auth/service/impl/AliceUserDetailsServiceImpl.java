package org.alice.auth.service.impl;

import org.alice.data.entity.UmsAccount;
import org.alice.auth.api.UmsServiceApi;
import org.alice.data.pojo.ActionResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Amse
 * @Date: 12/01/2021 17:52
 * @Version: 1.0.0
 */
@Component
public class AliceUserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UmsServiceApi umsServiceApi;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ActionResult<UmsAccount> account = umsServiceApi.getAccount(username);
        UmsAccount umsAccount = account.getData();
        if (umsAccount == null) {
            throw new UsernameNotFoundException("the user is not found");
        }
        String role = "ROLE_ADMIN";
        List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(role));
        return new User(username, umsAccount.getPassword(), authorities);
    }
}
