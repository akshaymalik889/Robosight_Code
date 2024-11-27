package com.example.TestingJWT.security;



import com.example.TestingJWT.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JWTService {

    //get secret key from our application.properties
    @Value("${jwt.secretKey}")
    private String jwtSecretKey;


    //this method used to convert our secret key into Object
    private SecretKey getSecretKey()
    {
        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(User user)
    {
        System.out.println("JWT_SECRET_KEY: " + jwtSecretKey);
        return Jwts.builder()
                .subject(user.getId().toString()) // store userId in subject
                .claim("email", user.getEmail())  // store email in claims , claims are key value pair
                .claim("roles", user.getRoles().toString())   // store roles in claims
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 *60 )) // 1 minutes
                .signWith(getSecretKey())  // provide secret key object
                .compact();
    }

    public String generateRefreshToken(User user)
    {
        return Jwts.builder()
                .subject(user.getId().toString()) // store userId in subject
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 30 * 6 )) // 6 months
                .signWith(getSecretKey())
                .compact();
    }

    // it takes token, and from subject it returns the userId (because in Subject we store userId while generating token)
    public Long getUserIdFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSecretKey())
                    //.setAllowedClockSkewSeconds(120) // Allow 2 minute of clock skew
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            System.out.println("Token Claims: " + claims); // Debugging log
            return Long.valueOf(claims.getSubject());
        } catch (Exception e) {
            System.err.println("Error Parsing Token: " + e.getMessage());
            throw new IllegalArgumentException("Invalid token");
        }
    }



}
