package com.damiane.accountservice.controller;

import com.damiane.accountservice.entity.Account;
import com.damiane.accountservice.model.LoginRequest;
import com.damiane.accountservice.service.AccountService;
import com.damiane.accountservice.utils.JwtTokenGenerator;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Key;
import java.util.Base64;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;

    @Autowired
    private JwtTokenGenerator jwtTokenGenerator;


    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/sign-in")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        // Perform user authentication based on credentials
        boolean isAuthenticated = accountService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());

        if (isAuthenticated) {
            // Generate JWT token upon successful authentication
            String jwtToken = jwtTokenGenerator.generateToken(loginRequest.getUsername(), accountService.getUserRoles(loginRequest.getUsername()));

            // Return the JWT token in the response
            return ResponseEntity.ok(jwtToken);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        Account createdAccount = accountService.createAccount(account);
        return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
    }

    @GetMapping("/{username}")
    public ResponseEntity<Account> getAccountByUsername(@PathVariable String username) {
        Account account = accountService.getAccountByUsername(username);
        if (account != null) {
            return new ResponseEntity<>(account, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Account>> getAllAccounts() {
        List<Account> accounts = accountService.getAllAccounts();
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long accountId) {
        accountService.deleteAccount(accountId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }



}
