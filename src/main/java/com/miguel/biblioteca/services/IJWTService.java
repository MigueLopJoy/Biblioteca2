package com.miguel.biblioteca.services;

import com.miguel.biblioteca.model.ULibrarian;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

public interface IJWTService {

    public String generateAccessToken(UserDetails userDetails);

    public String generateAccessToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    );

    public String generateRefreshToken(
            UserDetails userDetails
    );
    public void saveLibrarianToken(ULibrarian uLibrarian, String jwtToken);
    public void revokeAllLibrarianTokens(ULibrarian uLibrarian);
    public boolean isTokenValid(String token, UserDetails userDetails);
    public boolean isTokenExpired(String token);
    public Date extractExpiration(String token);
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver);
    public Claims extractAllClaims(String token);
    public String extractUserEmail(String token);
}
