package com.miguel.library.Config;

import com.miguel.library.model.Role;
import com.miguel.library.model.User;
import com.miguel.library.services.ImpUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomDetailsServiceTest {

    @Mock
    private ImpUserService userService;

    @InjectMocks
    private CustomDetailsService userDetailsService;

    @Test
    public void testLoadByUsername() {
        User manager = new User("John", "Doe", 'M', 1994, "example@gmail.com", "1234", "626100833");
        manager.setRole(Role.MANAGER);

        when(userService.searchByEmail("example@gmail.com")).thenReturn(manager);

        UserDetails userDetails = userDetailsService.loadUserByUsername("example@gmail.com");

        assertNotNull(userDetails);
        assertFalse(userDetails.getAuthorities().isEmpty());
        assertTrue(authorityExists(userDetails, "ROLE_MANAGER"));
        assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MANAGER")));
    }

    private boolean authorityExists(UserDetails userDetails, String authority) {
        return userDetails.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(authority));
    }


}