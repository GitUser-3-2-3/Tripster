package com.sc.authentication.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${backend.security.jwt.secret-key}")
    private static String SECRET_KEY;

    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        var grantedAuthorities = userDetails.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .toList();

        return Jwts.builder().claims(extraClaims).subject(userDetails.getUsername())
            .issuedAt(Date.from(Instant.now()))
            .expiration(Date.from(Instant.now().plus(30, ChronoUnit.DAYS)))
            .claim("authorities", grantedAuthorities)
            .signWith(getSignInKey()).compact();
    }

    public <R> R getClaims(String jwtToken, Function<Claims, R> claimResolver) {
        final Claims claims = getAllClaims(jwtToken);
        return claimResolver.apply(claims);
    }

    private Claims getAllClaims(String jwtToken) {
        return Jwts.parser().verifyWith(getSignInKey()).build()
            .parseSignedClaims(jwtToken)
            .getPayload();
    }

    public String getUsername(String jwtToken) {
        return getClaims(jwtToken, Claims::getSubject);
    }

    public Boolean isTokenExpired(String jwtToken) {
        return getExpirationDate(jwtToken).before(Date.from(Instant.now()));
    }

    private Date getExpirationDate(String jwtToken) {
        return getClaims(jwtToken, Claims::getExpiration);
    }

    public Boolean validateToken(String jwtToken, UserDetails userDetails) {
        final String username = getClaims(jwtToken, Claims::getSubject);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(jwtToken);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails);
    }

    private static SecretKey getSignInKey() {
        byte[] keyByte = Decoders.BASE64URL.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyByte);
    }
}