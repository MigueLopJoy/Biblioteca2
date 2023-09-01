package com.miguel.biblioteca.configurations;

import com.miguel.biblioteca.repositories.IULibrarianRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@AllArgsConstructor
@Configuration
public class ApplicationConfig {

    private final IULibrarianRepository uLibrarianRepository;


    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
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
                    .orElseThrow(() -> new UsernameNotFoundException("Librarian not found"));
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
