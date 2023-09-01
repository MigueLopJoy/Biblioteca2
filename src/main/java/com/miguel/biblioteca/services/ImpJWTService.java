package com.miguel.biblioteca.services;

import com.miguel.biblioteca.model.JWT;
import com.miguel.biblioteca.model.JWTType;
import com.miguel.biblioteca.model.ULibrarian;
import com.miguel.biblioteca.repositories.IJWTRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@AllArgsConstructor
@Service
public class ImpJWTService implements IJWTService {

    private final IJWTRepository tokenRepository;

    private final String secretKey = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
    private final long jwtExpiration = 3600000;
    private final long refreshExpiration = 86400000;

    public String generateAccessToken(UserDetails userDetails) {
        return generateAccessToken(new HashMap<>(), userDetails);
    }

    public String generateAccessToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    public String generateRefreshToken(
            UserDetails userDetails
    ) {
        return buildToken(new HashMap<>(), userDetails, refreshExpiration);
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public void saveLibrarianToken(ULibrarian uLibrarian, String jwtToken) {
        JWT jwt = JWT.builder()
                .uLibrarian(uLibrarian)
                .token(jwtToken)
                .jwtType(JWTType.BEARER)
                .expired(false)
                .revoked(false)
                .build();

        tokenRepository.save(jwt);
    }

    public void revokeAllLibrarianTokens(ULibrarian uLibrarian) {
        List<JWT> validLibrarianTokens = tokenRepository.findAllValidTokenByUser(uLibrarian.getIdUser());

        if (!validLibrarianTokens.isEmpty()) {
            validLibrarianTokens.forEach(jwt -> {
                jwt.setExpired(true);
                jwt.setRevoked(true);
            });
        }
        tokenRepository.saveAll(validLibrarianTokens);
    }

    /**
     * Extracts a specific claim from the JWT token using the provided claims resolver method from the Claims class.
     * @param token The JWT token from which to extract the claim.
     * @param claimsResolver The claims resolver to be used for extracting the claim.
     * @param <T> The type of claim to extract.
     *
     * @return The extracted claim from the token.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        // Extract all claims from the token
        final Claims claims = extractAllClaims(token);
        // Apply the method passed as a parameter to the created claims object to extract the specific Claim allowed by that method
        return claimsResolver.apply(claims);
    }
    public Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String userEmail = this.extractUserEmail(token);
        return (userEmail.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }
    public boolean isTokenExpired(String token) {
        return this.extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return this.extractClaim(token, Claims::getExpiration);
    }

    public String extractUserEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
