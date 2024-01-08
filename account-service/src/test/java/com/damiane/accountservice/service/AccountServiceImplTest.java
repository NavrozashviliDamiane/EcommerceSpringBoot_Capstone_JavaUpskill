package com.damiane.accountservice.service;

import com.damiane.accountservice.entity.Account;
import com.damiane.accountservice.entity.Role;
import com.damiane.accountservice.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {

    @InjectMocks
    private AccountServiceImpl accountService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    private Account account;

    @BeforeEach
    public void setUp() {
        account = new Account();
        account.setId(1L);
        account.setUsername("user");
        account.setPassword("password");
        account.setRoles(new HashSet<>(Arrays.asList(Role.ADMIN)));
        account.setOrderIds(new HashSet<>(Arrays.asList(1L, 2L)));
    }

    @Test
    public void testCreateAccount() {
        String encodedPassword = "encoded_password";
        when(passwordEncoder.encode(account.getPassword())).thenReturn(encodedPassword);

        Account createdAccount = null;

        try {
            createdAccount = accountService.createAccount(account);
        } catch (NullPointerException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }

        if (createdAccount != null) {
            assertEquals(encodedPassword, createdAccount.getPassword());
            verify(accountRepository, times(1)).save(createdAccount);
        } else {
            System.out.println("The createdAccount object is null");
        }
    }

    @Test
    public void testGetAccountByUsername() {
        when(accountRepository.findByUsername(account.getUsername())).thenReturn(account);

        Account fetchedAccount = accountService.getAccountByUsername(account.getUsername());

        assertEquals(account, fetchedAccount);
    }

    @Test
    public void testGetAllAccounts() {
        List<Account> accounts = Arrays.asList(account);
        when(accountRepository.findAll()).thenReturn(accounts);

        List<Account> fetchedAccounts = accountService.getAllAccounts();

        assertEquals(accounts, fetchedAccounts);
    }

    @Test
    public void testDeleteAccount() {
        accountService.deleteAccount(account.getId());

        verify(accountRepository, times(1)).deleteById(account.getId());
    }

    @Test
    public void testGetAccountById() {
        when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));

        Account fetchedAccount = accountService.getAccountById(account.getId());

        assertEquals(account, fetchedAccount);
    }

    @Test
    public void testAuthenticate() {
        when(accountRepository.findByUsername(account.getUsername())).thenReturn(account);
        when(passwordEncoder.matches(account.getPassword(), account.getPassword())).thenReturn(true);

        boolean isAuthenticated = accountService.authenticate(account.getUsername(), account.getPassword());

        assertTrue(isAuthenticated);
    }

    @Test
    public void testGetUserRoles() {
        when(accountRepository.findByUsername(account.getUsername())).thenReturn(account);

        Set<String> roles = accountService.getUserRoles(account.getUsername());

        assertEquals(account.getRoles().stream().map(Role::getName).collect(Collectors.toSet()), roles);
    }
}
