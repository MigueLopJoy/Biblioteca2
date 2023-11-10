package com.miguel.library.services;

import com.miguel.library.Exceptions.ExceptionNoSearchResultsFound;
import com.miguel.library.model.*;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface ITokenService {

    public Token saveToken(Token token);

    public void saveUserToken(User user, String jwtToken);

    public List<Token> saveAllTokens(List<Token> tokens);

    public Token generateUserTokenFromJwtString(String JwtToken, User user);

    public Token searchByToken(String token);

    public List<Token> searchAllValidTokensByUser(Integer userId);

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

    public void revokeAllUserTokens(User user);
}
