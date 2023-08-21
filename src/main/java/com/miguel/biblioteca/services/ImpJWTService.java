package com.miguel.biblioteca.services;

import com.miguel.biblioteca.model.JWT;
import com.miguel.biblioteca.model.ULibrarian;
import com.miguel.biblioteca.repositories.IJWTRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ImpJWTService implements IJWTService {

    private final IJWTRepository tokenRepository;

    private final JwtEncoder jwtEncoder;

    private final JwtDecoder jwtDecoder;
    private final long jwtExpiration = 3600000;
    private final long refreshExpiration = 86400000;

    public String generateRefreshToken(Authentication auth) { return generateJWT(auth, refreshExpiration); }
    public String generateAccessToken(Authentication auth) {
        return  generateJWT(auth, jwtExpiration);
    }

    /**
     * Generates a JSON Web Token (JWT) based on the provided Authentication object.
     *
     * @param auth The Authentication object representing the user's authentication details.
     *             It contains information about the user's identity, credentials, and granted authorities (roles or permissions).
     *             In Spring Security, successful authentication events lead to the creation of an Authentication object representing the authenticated user.
     *             This object encapsulates the user's authentication information and is typically retrieved from the security context.
     * @return A newly generated JWT string.
     */
    public String generateJWT(Authentication auth, long expirationTime) {

        // Get the current time as a reference for issuing and expiration timestamps.
        Instant now = Instant.now();

        /*
         * The 'scope' variable is populated by extracting the authorities from the 'Authentication' object.
         * The 'getAuthorities()' method retrieves a collection of 'GrantedAuthority' objects, representing the roles or permissions held by the user.
         * To process these authorities efficiently, the collection is transformed into a stream using 'stream()'.
         * The 'map()' operation extracts the authority name from each 'GrantedAuthority' object using a method reference 'GrantedAuthority::getAuthority'.
         * Finally, the 'collect()' operation gathers the mapped authority names into a single string using 'Collectors.joining(" ")', where authorities are separated by a space.
         */
        String scope = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        /*
         * Construct a Jwt
         * ClaimsSet object using the JwtClaimsSet.builder().
         * This builder allows you to assemble the claims of the JWT.
         * Here, the issuer is set to "self" (indicating that the token is self-issued),
         * issuedAt time is set to the value obtained earlier, subject is set to the name of the authenticated user (from auth.getName()),
         * and roles claim is set to the 'scope' value collected earlier.
         */
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plusMillis(expirationTime))
                .subject(auth.getName())
                .claim("roles", scope)
                .build();

        /*
         * Use the JwtEncoder instance to encode the JWT.
         * The encode() method of JwtEncoder expects a JwtEncoderParameters object as a parameter.
         * JwtEncoderParameters holds configuration options for encoding a JWT using a JwtEncoder, including claims and
         * settings. In this case, JwtEncoderParameters is created using JwtEncoderParameters.from(claims), where claims
         * is the JwtClaimsSet object. The encode() method then returns a Jwt object, from which getTokenValue() is
         * called to retrieve the JWT string.
         */
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public void saveLibrarianToken(ULibrarian uLibrarian, String jwtToken) {
        JWT jwt = new JWT();
        jwt.setULibrarian(uLibrarian);
        jwt.setToken(jwtToken);
        jwt.setExpired(false);
        jwt.setRevoked(false);

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
    /**
     * Extracts all claims from the provided JWT token.
     *
     * @param token The JWT token from which to extract the claims.
     * @return The extracted claims.
     *          The Claims object provides methods to access different claims present in the JWT token,
     *          such as subject, issuer, issuance and expiration dates, and any additional defined claims.
     */
    public Claims extractAllClaims(String token) {
        // JJWT library class that provides static methods to build, parse, and manipulate JWT tokens.
        return Jwts
                // Create an instance of JwtsParserBuilder, allowing various configurations to be set for the
                // JwtsParser before building it.
                .parserBuilder()
                /*
                Build the final JwtParser. This new object will be responsible for parsing and validating JWT tokens using
                the previously set configuration. It is an implementation of an interface that provides methods to parse,
                validate, and break down a JWT into its individual components (header, payload, and signature).
                It defines the main methods for working with JWT tokens.
                */
                .build()
                /*
                Receive a token as a parameter and parse it. If the token is valid and the signature is correct,
                a Jws<Claims> object is obtained. The Jws class represents a parsed and verified JWT token, containing
                both the header and the claims of the token.
                */
                .parseClaimsJws(token)
                /*
                Used on the Jws<Claims> object to get the claims from the JWT token.
                The getBody method returns a Claims object representing the claims contained within the JWT token.
                */
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

}
