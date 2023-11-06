package com.miguel.library.Config;

import com.miguel.library.Exceptions.ExceptionObjectNotFound;
import com.miguel.library.model.ULibrarian;
import com.miguel.library.model.UReader;
import com.miguel.library.model.User;
import com.miguel.library.services.IULibrarianService;
import com.miguel.library.services.IUReaderService;
import com.miguel.library.services.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Objects;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private IUserService userService;

    private UserDetailsService readerDetailsService() {
        return email -> {
            User user = userService.searchByEmail(email);
            if (Objects.isNull(user)) {
                throw new ExceptionObjectNotFound("User Not Found");
            }
            return user;
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(this.userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

