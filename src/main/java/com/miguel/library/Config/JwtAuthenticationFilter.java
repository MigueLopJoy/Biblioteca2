package com.miguel.library.Config;

import com.miguel.library.model.Token;
import com.miguel.library.services.ITokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

/*

@Component
@RequiredArgsConstructor
    public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private ITokenService tokenService;
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

        @Override
        protected void doFilterInternal(
                @NonNull HttpServletRequest request,
                @NonNull HttpServletResponse response,
                @NonNull FilterChain filterChain
        ) throws ServletException, IOException {
            if (request.getServletPath().contains("/auth")) {
                filterChain.doFilter(request, response);
                return;
            }

            String authHeader = request.getHeader("authorization");
            String jwt;
            String userEmail;

            if (Objects.isNull(authHeader) ||!authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            jwt = authHeader.substring(7);
            userEmail = tokenService.extractUsername(jwt);

            if (Objects.nonNull(userEmail) &&
                    Objects.isNull(SecurityContextHolder.getContext().getAuthentication())
            ) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
                Boolean isTokenValid = this.isTokenValid(jwt);

                if (tokenService.isTokenValid(jwt, userDetails) && isTokenValid) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                             userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            filterChain.doFilter(request, response);
        }

    private Boolean isTokenValid(String jwt) {
        Boolean isTokenValid = false;
        Token token = tokenService.searchByToken(jwt);
        if (!token.isExpired() && !token.isRevoked()) {
            isTokenValid = true;
        }
        return isTokenValid;
    }
}

*/
