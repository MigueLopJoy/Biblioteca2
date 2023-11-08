package com.miguel.library.services;

import com.miguel.library.Exceptions.ExceptionObjectNotFound;
import com.miguel.library.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ImpCustomDetailsService implements UserDetailsService {

    @Autowired
    private IUserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userService.searchByEmail(email);
        if (Objects.isNull(user)) {
            throw new ExceptionObjectNotFound("User Not Found");
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                this.getAuthorities(user)
        );
    }

    public List<SimpleGrantedAuthority> getAuthorities(User user) {
        var authorities = user.getRole().getAuthorities()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getAuthority()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
        return authorities;
    }

}
