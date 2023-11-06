package com.miguel.library.services;

import com.miguel.library.model.*;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;
import java.util.function.Function;

public interface ITokenService {

    public Token saveToken(User user, String jwtToken);

    public Token searchByToken(String token);

    public String extractUsername(String token);

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    public String generateToken(UserDetails userDetails);

    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    );
    public String generateRefreshToken(
            UserDetails userDetails
    );

    public boolean isTokenValid(String token, UserDetails userDetails);
}
