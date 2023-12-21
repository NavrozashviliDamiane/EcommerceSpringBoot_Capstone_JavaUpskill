package com.damiane.accountservice.service;

import com.damiane.accountservice.entity.Account;

import java.util.List;
import java.util.Set;

public interface AccountService {
    Account createAccount(Account account);
    Account getAccountByUsername(String username);
    List<Account> getAllAccounts();
    void deleteAccount(Long accountId);

    boolean authenticate(String username, String password);
    Set<String> getUserRoles(String username);

}

