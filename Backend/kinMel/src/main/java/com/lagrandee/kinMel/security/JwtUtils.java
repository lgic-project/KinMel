package com.lagrandee.kinMel.security;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.LocalDate;
import java.util.Date;
import java.nio.charset.StandardCharsets;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class JwtUtils {


    @Value("${kinMel.app.jwtSecret}")
    private String jwtSecret;
    @Value("${kinMel.app.jwtRefreshSecret}")
    private String jwtRefreshSecret;

    @Value("${kinMel.app.jwtExpirationMs}")
    private int jwtExpirationMs;
    @Value("${kinMel.app.jwtRefreshExpirationMs}")
    private int jwtRefreshExpirationMs;

    public String generateJwtToken(String username) {
        System.out.println("inside generate jwt token The time is now: "+LocalDate.now());
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public String generateJwtRefreshToken(String username) {
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + jwtRefreshExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload().getSubject();
    }

    public String getUserNameFromJwtRefreshToken(String token) {
        return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload().getSubject();
    }


    public boolean validateJwtToken(
            String authToken) throws SignatureException, MalformedJwtException, ExpiredJwtException, UnsupportedJwtException, IllegalArgumentException {
        try {

            Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(authToken);
            return true;
        } catch (SignatureException e) {
            System.out.println("Invalid JWT signature: {}"+e.getMessage());
            throw e;
        } catch (MalformedJwtException e) {
            System.out.println("Invalid JWT token: {}"+e.getMessage());
            throw e;
        } catch (ExpiredJwtException e) {
            System.out.println("JWT token is expired: {}"+e.getMessage());
            throw e;
        } catch (UnsupportedJwtException e) {
            System.out.println("JWT token is unsupported: {}"+e.getMessage());
            throw e;
        } catch (IllegalArgumentException e) {
            System.out.println("JWT claims string is empty: {}"+ e.getMessage());
            throw e;
        }
    }

    public boolean validateJwtRefreshToken(
            String authToken) throws SignatureException, MalformedJwtException, ExpiredJwtException, UnsupportedJwtException, IllegalArgumentException {
        try {
            Jwts.parser().setSigningKey(jwtRefreshSecret).build().parseSignedClaims(authToken);
            return true;
        } catch (SignatureException e) {
            System.out.println("Invalid JWT signature: {}"+e.getMessage());
            throw e;
        } catch (MalformedJwtException e) {
            System.out.println("Invalid JWT token: {}"+e.getMessage());
            throw e;
        } catch (ExpiredJwtException e) {
            System.out.println("JWT token is expired: {}"+e.getMessage());
            throw e;
        } catch (UnsupportedJwtException e) {
            System.out.println("JWT token is unsupported: {}"+e.getMessage());
            throw e;
        } catch (IllegalArgumentException e) {
            System.out.println("JWT claims string is empty: {}"+ e.getMessage());
            throw e;
        }
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = this.jwtSecret.getBytes(StandardCharsets.UTF_16);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
