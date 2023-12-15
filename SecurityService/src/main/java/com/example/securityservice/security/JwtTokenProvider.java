package com.example.securityservice.security;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpirationInMs;

    public String generateToken(Authentication authentication) throws KeyLengthException {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder();
        builder.subject(userDetails.getUsername());
        builder.expirationTime(new Date(System.currentTimeMillis() + jwtExpirationInMs));
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        builder.claim("roles", roles);

        JWTClaimsSet claimsSet = builder.build();
        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.HS256).build();
        SignedJWT signedJWT = new SignedJWT(header, claimsSet);

        JWSSigner signer = new MACSigner(jwtSecret);
        try {
            signedJWT.sign(signer);
        } catch (JOSEException e) {
            throw new RuntimeException("Failed to sign the JWT token", e);
        }

        return signedJWT.serialize();
    }

    public String getUsernameFromToken(String token) {
        try {
            JWTClaimsSet claimsSet = SignedJWT.parse(token).getJWTClaimsSet();
            return claimsSet.getSubject();
        } catch (ParseException e) {
            throw new RuntimeException("Failed to parse the JWT token", e);
        }
    }

    public boolean validateToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(jwtSecret);
            return signedJWT.verify(verifier);
        } catch (ParseException | JOSEException e) {
            return false;
        }
    }
}