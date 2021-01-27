package org.alice.admin.service.impl;

import org.alice.admin.mapper.AccountMapper;
import org.alice.admin.service.AccountService;
import org.alice.data.entity.Account;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author Amse
 * @version 1.0
 * @date  2021/1/16
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {
/*
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<UmsAccount> eq = Wrappers.<UmsAccount>lambdaQuery().eq(UmsAccount::getUsername, username);

        UmsAccount one = getOne(eq);
        if (one == null) {
            throw new UsernameNotFoundException(username);
        }
        return new User(username, one.getPassword(), Lists.newArrayList());
    }
*/

}
