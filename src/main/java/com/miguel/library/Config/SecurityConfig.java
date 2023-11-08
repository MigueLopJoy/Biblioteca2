package com.miguel.library.Config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import static com.miguel.library.model.Role.MANAGER;
import static com.miguel.library.model.Role.CATALOGER;
import static com.miguel.library.model.Role.LIBRARIAN;
import static com.miguel.library.model.Role.READER;

import static com.miguel.library.model.Permission.MANAGER_CREATE;
import static com.miguel.library.model.Permission.MANAGER_READ;
import static com.miguel.library.model.Permission.MANAGER_UPDATE;
import static com.miguel.library.model.Permission.MANAGER_DELETE;

import static com.miguel.library.model.Permission.CATALOGER_CREATE;
import static com.miguel.library.model.Permission.CATALOGER_READ;
import static com.miguel.library.model.Permission.CATALOGER_UPDATE;
import static com.miguel.library.model.Permission.CATALOGER_DELETE;

import static com.miguel.library.model.Permission.LIBRARIAN_CREATE;
import static com.miguel.library.model.Permission.LIBRARIAN_READ;
import static com.miguel.library.model.Permission.LIBRARIAN_UPDATE;
import static com.miguel.library.model.Permission.LIBRARIAN_DELETE;

import static com.miguel.library.model.Permission.READER_READ;
import static com.miguel.library.model.Permission.READER_UPDATE;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled = true)
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req
                                .requestMatchers("/auth/**").permitAll()
                                .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        ;
        return http.build();
    }
}
