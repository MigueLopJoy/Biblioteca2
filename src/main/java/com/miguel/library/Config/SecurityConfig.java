package com.miguel.library.Config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
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
public class SecurityConfig {

    private static final String[] WHITE_LIST_URL = {
            "/auth/**",
            "/authors/",
            "/librarians/",
            "/bookworks-catalog/",
            "/library/"
    };

    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req.requestMatchers(WHITE_LIST_URL)
                                .permitAll()
                                .requestMatchers("/librarians/**").hasAnyRole(MANAGER.name(), CATALOGER.name(), LIBRARIAN.name())
                                .requestMatchers(GET, "/librarians/**").hasAnyAuthority(MANAGER_READ.name(), CATALOGER_READ.name(), LIBRARIAN_READ.name())
                                .requestMatchers(POST, "/librarians/save-librarian").hasAnyAuthority(MANAGER_CREATE.name())
                                .requestMatchers(PUT, "/librarians/**").hasAnyAuthority(MANAGER_UPDATE.name(), CATALOGER_UPDATE.name(), LIBRARIAN_UPDATE.name())
                                .requestMatchers(DELETE, "/librarians/**").hasAnyAuthority(MANAGER_DELETE.name())
                                .anyRequest()
                                .permitAll()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        ;
        return http.build();
    }
}
