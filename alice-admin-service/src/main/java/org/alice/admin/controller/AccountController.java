package org.alice.admin.controller;

import org.alice.data.entity.UmsAccount;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.alice.admin.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Amse
 * @version 1.0
 * @date 2021/1/17
 */
@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/getAccount/{username}")
    public UmsAccount getAccountByUsername(@PathVariable("username") final String username) {
        LambdaQueryWrapper<UmsAccount> eq = Wrappers.<UmsAccount>lambdaQuery().eq(UmsAccount::getUsername, username);
        return accountService.getOne(eq);
    }

}

