package com.miguel.biblioteca.configurations;

import com.miguel.biblioteca.services.IJWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 *  This class is used as an authentication filter to verify and process JWT tokens in incoming HTTP requests.
 *
 *  OncePerRequestFilter is an abstract class provided by Spring Security, extending the GenericFilterBean class.
 *  It simplifies the implementation of custom filters by ensuring that the doFilter() method of the filter is invoked
 *  only once per request. When you extend the OncePerRequestFilter class and implement its doFilterInternal() method,
 *  Spring Security ensures that the filter is invoked only once per request, regardless of how many times the filter
 *  might be called within the filter chain.
 */
@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final IJWTService jwtService;
    private final UserDetailsService userDetailsService;

    /**
     * Verifies the presence and validity of the JWT token in the authorization header of the HTTP request.
     *
     * @param request       The incoming HTTP request.
     * @param response      The HTTP response.
     * @param filterChain  The chain of filters applied sequentially to the HTTP request.
     * @throws ServletException If an error occurs during the execution of the filter.
     * @throws IOException If an I/O error occurs during the execution of the filter.
         */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        // Extract the authorization header from the HTTP request
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        // Check that the authorization header is not null and follows the Bearer scheme
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // doFilter passes the request and response to the next filter in the chain or the final servlet
            // that will handle the request.
            filterChain.doFilter(request, response);
            return;
        }
        // Extract the token from the HTTP request, starting from position 7 (Bearer XXX...)
        jwt = authHeader.substring(7);
        // Get the username (email) from the token
        userEmail = jwtService.extractUserEmail(jwt);
        // Check that userEmail is not null and the user has not been authenticated previously.
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // If the user is not authenticated, get UserDetails from the database.
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            // Check if the token is valid.
            if (jwtService.isTokenValid(jwt, userDetails)) {
                /*
                If the token is valid, create an object of type UsernamePasswordAuthenticationToken, representing an
                authentication request. It is used to encapsulate and transport user authentication information during
                the authentication process.
                */
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        // No additional credentials (password) are required as authentication was done using the JWT.
                        null,
                        /*
                        The authorities (roles or privileges) obtained from the UserDetails object are passed to the
                        UsernamePasswordAuthenticationToken constructor to assign roles or privileges to the authenticated
                        user.
                        */
                        userDetails.getAuthorities()
                );
                /*
                Set authentication details in the authToken object using the WebAuthenticationDetailsSource class.
                This is used to create WebAuthenticationDetails objects containing additional information related to
                web-based authentication details, such as client IP address and user agent information.
                The buildDetails(request) method is responsible for creating the WebAuthenticationDetails object from
                the request.
                */
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                /*
                Set the current authentication in the security context using the authToken object.
                The SecurityContextHolder class is used to access and manipulate the current security context in the
                application.
                The getContext() method returns the current security context.
                The setAuthentication(authToken) method is used to set the authentication in the security context. Here,
                the authToken object representing JWT-based user authentication is passed.
                */
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        // Proceed to execute the next filters in the chain.
        filterChain.doFilter(request, response);
    }
}

