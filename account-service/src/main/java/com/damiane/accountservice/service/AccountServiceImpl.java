package com.damiane.accountservice.service;

import com.damiane.accountservice.entity.Account;
import com.damiane.accountservice.entity.Role;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.damiane.accountservice.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final BCryptPasswordEncoder passwordEncoder;


    public AccountServiceImpl(AccountRepository accountRepository, BCryptPasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Account createAccount(Account account) {
        String hashedPassword = passwordEncoder.encode(account.getPassword());
        account.setPassword(hashedPassword);
        return accountRepository.save(account);
    }

    @Override
    public Account getAccountByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public void deleteAccount(Long accountId) {
        accountRepository.deleteById(accountId);
    }

    @Override
    public Account getAccountById(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with ID: " + accountId));
    }

    @Override
    public boolean authenticate(String username, String password) {
        Account account = accountRepository.findByUsername(username);
        return account != null && passwordEncoder.matches(password, account.getPassword());
    }

    @Override
    public Set<String> getUserRoles(String username) {
        Account account = accountRepository.findByUsername(username);
        return account != null ? account.getRoles().stream().map(Role::getName).collect(Collectors.toSet()) : Collections.emptySet();
    }


    @Override
    public void addOrderIds(Long accountId, Long newOrderId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with ID: " + accountId));

        Set<Long> existingOrderIds = account.getOrderIds();
        existingOrderIds.add(newOrderId);

        account.setOrderIds(existingOrderIds);

        accountRepository.save(account);
    }


}

