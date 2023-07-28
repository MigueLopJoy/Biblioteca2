package com.miguel.biblioteca.services;

import com.miguel.biblioteca.DTO.ULibrarianDTO;
import com.miguel.biblioteca.mapper.ULibrarianMapper;
import com.miguel.biblioteca.model.Role;
import com.miguel.biblioteca.model.ULibrarian;
import com.miguel.biblioteca.repositories.IULibrarianRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.given;

class ImpULibrarianServiceTest {

    @Mock
    private IULibrarianRepository uLibrarianRepository;

    @Mock
    private ULibrarianMapper uLibrarianMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Captor
    private ArgumentCaptor<ULibrarian> uLibrarianArgumentCaptor;

    private IULibrarianService underTest;

    @BeforeEach
    void SetUp() {
        MockitoAnnotations.initMocks(this);
        this.underTest = new ImpULibrarianService(uLibrarianRepository, uLibrarianMapper, passwordEncoder);
    }

    @Test
    void itShouldSignUpNewLibrarian() {
        // Given...

            // ... some user's authorities
        Role role = new Role("MANAGER");
        Set<Role> authorities = new HashSet<>();
        authorities.add(role);

            // ... A new librarian data contained in a DTO

        String firstName = "Miguel";
        String lastName = "Lopez";
        String userPhoneNumber = "626100833";
        String userEmail = "m.l.r@gmail.com";
        String password = "1234";
        ULibrarianDTO uLibrarianDTO = new ULibrarianDTO(firstName, lastName, userPhoneNumber, userEmail, password);


        given(uLibrarianMapper.mapDtoToEntity(uLibrarianDTO)).willReturn(new ULibrarian());
        given(passwordEncoder.encode(password)).willReturn("encodedPassword");
        given(uLibrarianRepository.findByUserEmail(userEmail)).willReturn(Optional.empty());
        given(uLibrarianRepository.findByUserPhoneNumber(userPhoneNumber)).willReturn(Optional.empty());


        // When
        ULibrarianDTO savedLibrarianDTO = underTest.signUpNewLibrarian(uLibrarianDTO, authorities);


        // Then
        then(uLibrarianRepository).should().save(uLibrarianArgumentCaptor.capture());
        ULibrarian uLibrarianArgumentCaptorValue = uLibrarianArgumentCaptor.getValue();
        assertThat(uLibrarianArgumentCaptorValue).isEqualToComparingFieldByField(uLibrarianDTO);
    }
}