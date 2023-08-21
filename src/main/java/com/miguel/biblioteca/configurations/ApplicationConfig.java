package com.miguel.biblioteca.configurations;

import com.miguel.biblioteca.repositories.IULibrarianRepository;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

@AllArgsConstructor
@Configuration
public class ApplicationConfig {

    private final IULibrarianRepository uLibrarianRepository;

    private final RSAKeyProvider keys;

    /**
     * This method constructs and configures an AuthenticationManager instance for user authentication.
     * The method takes a UserDetailsService as a parameter, which provides user details for authentication.
     *
     * A new DaoAuthenticationProvider instance is created. This provider is an implementation of the AuthenticationProvider interface.
     * It authenticates users by querying a database using the provided UserDetailsService object.
     *
     * The UserDetailsService for the DaoAuthenticationProvider is set to the provided userDetailsService parameter.
     * This allows the provider to retrieve user details for authentication purposes.
     *
     * The passwordEncoder() method is called to retrieve an instance of a password encoder.
     * The retrieved password encoder is set for the DaoAuthenticationProvider to ensure proper password handling during authentication.
     *
     * The method then creates a ProviderManager instance, which is responsible for delegating authentication requests to one or more AuthenticationProvider objects.
     * In this context, the ProviderManager is configured with the previously created DaoAuthenticationProvider.
     * This means that the authentication process is delegated to the DaoAuthenticationProvider for user authentication.
     *
     * Finally, the configured ProviderManager instance is returned as the AuthenticationManager bean, enabling the authentication process in the application.
     */
    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(daoAuthenticationProvider);
    }

    /**
     * This method defines and configures a UserDetailsService bean that is responsible for retrieving user details based on their email.
     * The returned UserDetailsService instance is used by Spring Security for user authentication and authorization purposes.
     *
     * @return A UserDetailsService instance for retrieving user details based on email.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return userEmail -> {
            /*
             * The provided lambda expression takes a userEmail parameter and uses it to query the uLibrarianRepository.
             * The uLibrarianRepository is expected to provide user details based on the given email.
             *
             * The 'findByUserEmail' method attempts to find a user based on the provided email.
             * If a user is found, the 'orElseThrow' method is used to provide an exception supplier.
             * The exception supplier creates and throws a 'UsernameNotFoundException' with the message "User not found".
             * This exception is thrown when the user is not found in the repository.
             *
             * If a user is found in the repository, their details are returned, providing the necessary user information
             * to Spring Security for authentication and authorization processes.
             *
             * Note: This implementation assumes that 'uLibrarianRepository' is a repository that interacts with user data,
             * and its 'findByUserEmail' method returns an Optional containing the user details or an empty Optional.
             * The returned UserDetailsService instance will be used by Spring Security to retrieve user details for authentication.
             */
            return uLibrarianRepository.findByUserEmail(userEmail)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        };
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Creates and configures a bean for JWT decoding using the Nimbus library's NimbusJwtDecoder implementation.
     * Initializes the decoder with a public key obtained from the 'keys' object.
     *
H     * @return A configured JwtDecoder bean for decoding JWTs.
     */
    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(keys.getPublicKey()).build();
    }

    /**
     * Creates a bean for JWT encoding using the provided RSA public and private keys object.
     *
     * This method constructs a JWK (JSON Web Key) object representing cryptographic keys, including public and private keys.
     * JWK is a JSON-based format used in web-based security protocols, including JSON Web Tokens (JWTs).
     * The constructed JWK here is specific to RSA keys.
     *
     * @return A configured JwtEncoder bean for encoding JWTs.
     */
    @Bean
    public JwtEncoder jwtEncoder() {
        /*
         * Create a JWK (JSON Web Key) object using the provided RSA public and private keys.
         * A JWK represents cryptographic keys in JSON format, suitable for various security protocols.
         * In this case, it's an RSA key.
         */
        JWK jwk = new RSAKey.Builder(keys.getPublicKey()).privateKey(keys.getPrivateKey()).build();

        /*
         * Construct an ImmutableJWKSet using the JWK object, providing a source of keys.
         * This JWKSource is required for JWT encoding.
         */
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));

        // Return a NimbusJwtEncoder configured with the JWK source for JWT encoding.
        return new NimbusJwtEncoder(jwks);
    }


    /**
     * This method configures a Spring Bean responsible for converting JWT claims into Spring Security GrantedAuthority
     * objects, which are used in the authentication and authorization processes of a Spring Boot application.      *
     *
     * @return A JwtAuthenticationConverter configured to extract roles (authorities) from JWT claims.
     */    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        // Create a new instance of JwtGrantedAuthoritiesConverter.
        // This converter is used to extract roles (authorities) from the JWT claims.
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

        // Specify the name of the claim in the JWT payload where roles (authorities) are stored.
        // This is the claim key that contains information about the roles the user has.
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles");

        // Set a prefix that will be added to each authority/role extracted from the JWT.
        // This is a common practice to distinguish roles from other types of authorities.
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        // Create a new instance of JwtAuthenticationConverter.
        // This converter combines JWT claim extraction with the authentication process.
        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();

        // Attach the JwtGrantedAuthoritiesConverter to the JwtAuthenticationConverter.
        // This links the process of extracting roles from the JWT to the authentication process.
        jwtConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        // Return the configured JwtAuthenticationConverter bean.
        // This bean is now ready to be used in the authentication process.
        return jwtConverter;
    }
}
