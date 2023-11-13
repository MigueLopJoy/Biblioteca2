package com.miguel.library.Config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private LogoutHandler logoutHandler;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req
                                .requestMatchers("/auth/**").permitAll()
                                .requestMatchers("/test/get-headers").permitAll()
                                .requestMatchers("/test/**").permitAll()

                                .requestMatchers("/librarians/save-librarian")
                                        .hasAuthority("ROLE_MANAGER")
                                .requestMatchers("/librarians/search-librarian")
                                        .hasAnyAuthority("ROLE_MANAGER", "ROLE_CATALOGER", "ROLE_LIBRARIAN")
                                .requestMatchers("/librarians/get-librarian-account")
                                        .hasAnyAuthority("ROLE_MANAGER", "ROLE_CATALOGER", "ROLE_LIBRARIAN")
                                .requestMatchers("/librarians/edit-librarian")
                                        .hasAnyAuthority("ROLE_MANAGER", "ROLE_CATALOGER", "ROLE_LIBRARIAN")
                                .requestMatchers("/librarians/delete-librarian")
                                        .hasAuthority("ROLE_MANAGER")

                                .requestMatchers("/readers/save-reader")
                                        .hasAnyAuthority("ROLE_MANAGER", "ROLE_CATALOGER", "ROLE_LIBRARIAN")
                                .requestMatchers("/readers/search-reader")
                                        .hasAnyAuthority("ROLE_MANAGER", "ROLE_CATALOGER", "ROLE_LIBRARIAN")
                                .requestMatchers("/readers/get-reader-account")
                                        .hasAnyAuthority("ROLE_MANAGER", "ROLE_CATALOGER", "ROLE_LIBRARIAN")
                                .requestMatchers("/readers/edit-reader")
                                    .hasAnyAuthority("ROLE_MANAGER", "ROLE_CATALOGER", "ROLE_LIBRARIAN")
                                .requestMatchers("/readers/delete-reader")
                                    .hasAnyAuthority("ROLE_MANAGER", "ROLE_CATALOGER", "ROLE_LIBRARIAN")

                                .requestMatchers("/authors/save-author")
                                        .hasAnyAuthority("ROLE_MANAGER", "ROLE_CATALOGER")
                                .requestMatchers("/authors/search-author")
                                        .hasAnyAuthority("ROLE_MANAGER", "ROLE_CATALOGER", "ROLE_LIBRARIAN", "ROLE_READER")
                                .requestMatchers("/authors/get-author-bookworks")
                                        .hasAnyAuthority("ROLE_MANAGER", "ROLE_CATALOGER", "ROLE_LIBRARIAN", "ROLE_READER")
                                .requestMatchers("/authors/edit-author")
                                        .hasAnyAuthority("ROLE_MANAGER", "ROLE_CATALOGER")
                                .requestMatchers("/authors/delete-author")
                                        .hasAnyAuthority("ROLE_MANAGER", "ROLE_CATALOGER")

                                .requestMatchers("/bookworks-catalog/save-bookwork")
                                        .hasAnyAuthority("ROLE_MANAGER", "ROLE_CATALOGER")
                                .requestMatchers("/bookworks-catalog/search-bookwork")
                                        .hasAnyAuthority("ROLE_MANAGER", "ROLE_CATALOGER", "ROLE_LIBRARIAN", "ROLE_READER")
                                .requestMatchers("/bookworks-catalog/get-bookwork-editions")
                                        .hasAnyAuthority("ROLE_MANAGER", "ROLE_CATALOGER", "ROLE_LIBRARIAN", "ROLE_READER")
                                .requestMatchers("/bookworks-catalog/edit-bookwork")
                                        .hasAnyAuthority("ROLE_MANAGER", "ROLE_CATALOGER")
                                .requestMatchers("/bookworks-catalog/delete-bookwork")
                                        .hasAnyAuthority("ROLE_MANAGER", "ROLE_CATALOGER")

                                .requestMatchers("/general-catalog/save-bookedition")
                                        .hasAnyAuthority("ROLE_MANAGER", "ROLE_CATALOGER")
                                .requestMatchers("/general-catalog/search-bookeditions")
                                        .hasAnyAuthority("ROLE_MANAGER", "ROLE_CATALOGER", "ROLE_LIBRARIAN", "ROLE_READER")
                                .requestMatchers("/general-catalog/get-bookwork-editions")
                                        .hasAnyAuthority("ROLE_MANAGER", "ROLE_CATALOGER", "ROLE_LIBRARIAN", "ROLE_READER")
                                .requestMatchers("/general-catalog/edit-bookedition")
                                        .hasAnyAuthority("ROLE_MANAGER", "ROLE_CATALOGER")
                                .requestMatchers("/general-catalog/delete-bookedition")
                                        .hasAnyAuthority("ROLE_MANAGER", "ROLE_CATALOGER")

                                .requestMatchers("/bookcopies/save-bookcopy")
                                        .hasAnyAuthority("ROLE_MANAGER", "ROLE_CATALOGER", "ROLE_LIBRARIAN")
                                .requestMatchers("/bookcopies/search-bookcopy")
                                        .hasAnyAuthority("ROLE_MANAGER", "ROLE_CATALOGER", "ROLE_LIBRARIAN", "ROLE_READER")
                                .requestMatchers("/bookcopies/edit-bookcopy")
                                        .hasAnyAuthority("ROLE_MANAGER", "ROLE_CATALOGER", "ROLE_LIBRARIAN")
                                .requestMatchers("/bookcopies/delete-bookcopy")
                                        .hasAnyAuthority("ROLE_MANAGER", "ROLE_CATALOGER", "ROLE_LIBRARIAN")

                                .requestMatchers("/library/search-library")
                                        .hasAnyAuthority("ROLE_MANAGER", "ROLE_CATALOGER", "ROLE_LIBRARIAN", "ROLE_READER")

                                .requestMatchers("/users/change-password")
                                        .hasAnyAuthority("ROLE_MANAGER", "ROLE_CATALOGER", "ROLE_LIBRARIAN", "ROLE_READER")
                                .requestMatchers("/users/logout")
                                        .hasAnyAuthority("ROLE_MANAGER", "ROLE_CATALOGER", "ROLE_LIBRARIAN", "ROLE_READER")
                                .requestMatchers("/users/get-connected-user")
                                        .hasAnyAuthority("ROLE_MANAGER", "ROLE_CATALOGER", "ROLE_LIBRARIAN", "ROLE_READER")

                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout ->
                        logout
                                .logoutUrl("/users/logout")
                                .addLogoutHandler(logoutHandler)
                                .logoutSuccessHandler((new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK)))
                )
        ;
        return http.build();
    }
}