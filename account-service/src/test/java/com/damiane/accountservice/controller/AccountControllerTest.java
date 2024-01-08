package com.damiane.accountservice.controller;

import com.damiane.accountservice.entity.Account;
import com.damiane.accountservice.model.LoginRequest;
import com.damiane.accountservice.service.AccountService;
import com.damiane.accountservice.utils.JwtTokenGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountControllerTest {

    @InjectMocks
    private AccountController accountController;

    @Mock
    private AccountService accountService;

    private Account account;


    @Mock
    private JwtTokenGenerator jwtTokenGenerator;

    @BeforeEach
    public void setUp() {
        accountController = new AccountController(accountService);
    }



    @Test
    public void login_shouldReturnUnauthorized_whenCredentialsAreInvalid() {
        String username = "user";
        String password = "password";

        LoginRequest loginRequest = new LoginRequest(username, password);

        when(accountService.authenticate(username, password)).thenReturn(false);

        ResponseEntity<String> response = accountController.login(loginRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void getAllAccounts_shouldReturnAllAccounts() {
        List<Account> accounts = Collections.singletonList(new Account());

        when(accountService.getAllAccounts()).thenReturn(accounts);

        ResponseEntity<List<Account>> response = accountController.getAllAccounts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(accounts, response.getBody());
    }

    @Test
    public void deleteAccount_shouldReturnNoContent_whenAccountDeleted() {
        account = new Account();
        account.setId(1L);
        account.setUsername("username");
        account.setPassword("password");

        doNothing().when(accountService).deleteAccount(account.getId());

        ResponseEntity<Void> response = accountController.deleteAccount(account.getId());

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void getAccountById_shouldReturnAccount_whenAccountExists() {
        account = new Account();
        account.setId(1L);
        account.setUsername("username");
        account.setPassword("password");

        when(accountService.getAccountById(account.getId())).thenReturn(account);

        ResponseEntity<Account> response = accountController.getAccountById(account.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(account, response.getBody());
    }

    @Test
    public void addOrderIdsToAccount_shouldReturnOk_whenOrderIdsAdded() {

        account = new Account();
        account.setId(1L);
        account.setUsername("username");
        account.setPassword("password");
        doNothing().when(accountService).addOrderIds(account.getId(), 1L);

        ResponseEntity<String> response = accountController.addOrderIdsToAccount(account.getId(), 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Order IDs added to account successfully.", response.getBody());
    }
}
