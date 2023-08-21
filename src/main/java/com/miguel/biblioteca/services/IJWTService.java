package com.miguel.biblioteca.services;

import com.miguel.biblioteca.model.ULibrarian;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

public interface IJWTService {

    public String generateRefreshToken(Authentication auth);
    public String generateAccessToken(Authentication auth);
    public String generateJWT(Authentication auth, long expirationTime);
    public void saveLibrarianToken(ULibrarian uLibrarian, String jwtToken);
    public void revokeAllLibrarianTokens(ULibrarian uLibrarian);
    public boolean isTokenValid(String token, UserDetails userDetails);
    public boolean isTokenExpired(String token);
    public Date extractExpiration(String token);
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver);
    public Claims extractAllClaims(String token);
    public String extractUserEmail(String token);
}
