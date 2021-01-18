package org.alice.ums.service.impl;

import org.alice.ums.mapper.UmsAccountMapper;
import org.alice.ums.service.UmsAccountService;
import com.alice.data.entity.UmsAccount;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author Amse
 * @version 1.0
 * @date  2021/1/16
 */
@Service
public class UmsAccountServiceImpl extends ServiceImpl<UmsAccountMapper, UmsAccount> implements UmsAccountService {
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
