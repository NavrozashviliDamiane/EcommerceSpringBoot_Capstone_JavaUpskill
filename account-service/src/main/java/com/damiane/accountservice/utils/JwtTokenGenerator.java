package com.damiane.accountservice.utils;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.Set;

@Component
public class JwtTokenGenerator {

    private final byte[] secretKey;

    @Autowired
    public JwtTokenGenerator(@Value("${jwt.secret-key}") String encodedSecretKey) {
        this.secretKey = decodeSecretKey(encodedSecretKey);
    }

    private byte[] decodeSecretKey(String encodedKey) {
        try {
            return Base64.getDecoder().decode(encodedKey);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid base64 encoded secret key", e);
        }
    }


    Date expirationTime = new Date(System.currentTimeMillis() + 3600 * 10000);

    public String generateToken(String username, Set<String> roles) {
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(expirationTime)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}


