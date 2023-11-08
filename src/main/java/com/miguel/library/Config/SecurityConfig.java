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

                                .requestMatchers("librarians/save-librarian").hasAuthority("ROLE_MANAGER")
                                .requestMatchers("librarians/search-librarian").hasAuthority("ROLE_MANAGER")
                                .requestMatchers("librarians/edit-librarian").hasAuthority("ROLE_MANAGER")
                                .requestMatchers("librarians/delete-librarian").hasAuthority("ROLE_MANAGER")

                                .requestMatchers("readers/save-reader").hasAuthority("ROLE_MANAGER")
                                .requestMatchers("readers/search-reader").hasAuthority("ROLE_MANAGER")
                                .requestMatchers("readers/edit-reader").hasAuthority("ROLE_MANAGER")
                                .requestMatchers("readers/delete-reader").hasAuthority("ROLE_MANAGER")

                                .requestMatchers("authors/save-authors").hasAuthority("ROLE_MANAGER")
                                .requestMatchers("authors/search-authors").hasAuthority("ROLE_MANAGER")
                                .requestMatchers("authors/edit-authors").hasAuthority("ROLE_MANAGER")
                                .requestMatchers("authors/delete-authors").hasAuthority("ROLE_MANAGER")

                                .requestMatchers("bookworks-catalog/save-bookworks").hasAuthority("ROLE_MANAGER")
                                .requestMatchers("bookworks-catalog/search-bookworks").hasAuthority("ROLE_MANAGER")
                                .requestMatchers("bookworks-catalog/edit-bookworks").hasAuthority("ROLE_MANAGER")
                                .requestMatchers("bookworks-catalog/delete-bookworks").hasAuthority("ROLE_MANAGER")

                                .requestMatchers("bookeditions-catalog/save-librarian").hasAuthority("ROLE_MANAGER")
                                .requestMatchers("bookeditions-catalog/search-librarian").hasAuthority("ROLE_MANAGER")
                                .requestMatchers("bookeditions-catalog/edit-librarian").hasAuthority("ROLE_MANAGER")
                                .requestMatchers("bookeditions-catalog/delete-librarian").hasAuthority("ROLE_MANAGER")

                                .requestMatchers("bookcopies/save-librarian").hasAuthority("ROLE_LIBRARIAN")
                                .requestMatchers("bookcopies/search-librarian").hasAuthority("ROLE_MANAGER")
                                .requestMatchers("bookcopies/edit-librarian").hasAuthority("ROLE_MANAGER")
                                .requestMatchers("bookcopies/delete-librarian").hasAuthority("ROLE_MANAGER")

                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        ;
        return http.build();
    }
}
