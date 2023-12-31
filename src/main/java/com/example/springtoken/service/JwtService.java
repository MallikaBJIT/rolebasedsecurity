package com.example.springtoken.service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.example.springtoken.exception.CustomException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.csrf.InvalidCsrfTokenException;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.util.Base64;

import org.springframework.security.core.userdetails.UserDetails;

@Service
public class JwtService {
    private static final String code = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // generate token only from userdetails
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
    	List<String> roles = userDetails.getAuthorities()
    									.stream()
    									.map(role -> role.getAuthority().toUpperCase())
    									.toList();
        return Jwts.builder().setClaims(extraClaims)
        		.claim("role", roles)
        		.setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 20 * 60))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256).compact();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claim = extractAllClaims(token);
        return claimResolver.apply(claim);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
    }

    public Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(code);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        if (isTokenExpired(token) && userName.equals(userDetails.getUsername())) {
            throw new CustomException("Invalid token", HttpStatus.UNAUTHORIZED);
        }
        return true;
    }

    private boolean isTokenExpired(String token) {
        if (extractExpirationDate(token).before(new Date())) {
            throw new CustomException("Token is expired", HttpStatus.UNAUTHORIZED);
        }
        return false;
    }

    private Date extractExpirationDate(String token) {
        // TODO Auto-generated method stub
        return extractClaim(token, Claims::getExpiration);
    }
}
