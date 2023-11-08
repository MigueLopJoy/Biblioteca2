package com.miguel.library.services;

import com.miguel.library.model.Role;
import com.miguel.library.model.User;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CustomUserDetailsServiceTest {

    @Mock
    private ImpUserService userService;

    @InjectMocks
    private ImpCustomDetailsService userDetailsService;

    @Test
    public void testLoadByUsername() {
        User manager = new User("John", "Doe", 'M', 1994, "example@example.com", "1234", "626100833");
        manager.setRole(Role.MANAGER);

        when(userService.searchByEmail("example@example.com")).thenReturn(manager);

        UserDetails userDetails = userDetailsService.loadUserByUsername("\"example@example.com\"");

        assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MANAGER")));
    }
}