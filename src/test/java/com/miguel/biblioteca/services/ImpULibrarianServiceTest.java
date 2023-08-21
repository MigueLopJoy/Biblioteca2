package com.miguel.biblioteca.services;

import com.miguel.biblioteca.DTO.RoleDTO;
import com.miguel.biblioteca.DTO.ULibrarianDTO;
import com.miguel.biblioteca.mapper.ULibrarianMapper;
import com.miguel.biblioteca.model.Role;
import com.miguel.biblioteca.model.ULibrarian;
import com.miguel.biblioteca.repositories.IRoleRepository;
import com.miguel.biblioteca.repositories.IULibrarianRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class ImpULibrarianServiceTest {
    @Mock
    private IULibrarianRepository uLibrarianRepository;
    @Mock
    private IRoleRepository roleRepository;
    @Mock
    private ULibrarianMapper uLibrarianMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private ImpULibrarianService underTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void itShouldSignUpLibraryManagerWithUniqueDetails() {
        // Given...
        String firstName = "firstName";
        String lastName = "lastName";
        String userPhoneNumber = "userPhoneNumber";
        String userEmail = "userEmail";
        String password = "password";
        String encodedPassword = "encoded_password";

        Set<Role> authorities = new HashSet<>();
        Role librarianRole = new Role("LIBRARIAN");
        Role managerRole = new Role("MANAGER");
        authorities.add(librarianRole);
        authorities.add(managerRole);

        Set<RoleDTO> authoritiesDTO = new HashSet<>();
        RoleDTO librarianRoleDTO = new RoleDTO("LIBRARIAN");
        RoleDTO managerRoleDTO = new RoleDTO("MANAGER");
        authoritiesDTO.add(librarianRoleDTO);
        authoritiesDTO.add(managerRoleDTO);

        ULibrarianDTO uLibrarianDTORequest
                = new ULibrarianDTO(firstName, lastName, userPhoneNumber, userEmail, password);

        ULibrarian mappedULibrarian
                = new ULibrarian(firstName, lastName, userPhoneNumber, userEmail, password);

        ULibrarian savedULibrarian
                = new ULibrarian(firstName, lastName, userPhoneNumber, userEmail, encodedPassword, authorities);

        ULibrarianDTO savedULibrarianDTO
                = new ULibrarianDTO(firstName, lastName, userPhoneNumber, userEmail, encodedPassword, authoritiesDTO);

        given(uLibrarianMapper.mapDtoToEntity(uLibrarianDTORequest)).willReturn(mappedULibrarian);
        given(roleRepository.findByAuthority(librarianRole.getAuthority())).willReturn(Optional.of(librarianRole));
        given(roleRepository.findByAuthority(managerRole.getAuthority())).willReturn(Optional.of(managerRole));
        given(passwordEncoder.encode(password)).willReturn(encodedPassword);
        given(uLibrarianRepository.findByUserEmail(userEmail)).willReturn(Optional.empty());
        given(uLibrarianRepository.findByUserPhoneNumber(userPhoneNumber)).willReturn(Optional.empty());
        given(uLibrarianRepository.save(any(ULibrarian.class))).willReturn(savedULibrarian);
        given(uLibrarianMapper.mapEntityToDto(any(ULibrarian.class))).willReturn(savedULibrarianDTO);

        // When
        ULibrarianDTO responseULibrarianDTO = underTest.signUpLibraryManager(uLibrarianDTORequest);

        // Then

        then(uLibrarianMapper).should().mapDtoToEntity(uLibrarianDTORequest);
        then(roleRepository).should().findByAuthority(librarianRole.getAuthority());
        then(roleRepository).should().findByAuthority(managerRole.getAuthority());
        then(passwordEncoder).should().encode(password);
        then(uLibrarianRepository).should().findByUserEmail(userEmail);
        then(uLibrarianRepository).should().findByUserPhoneNumber(userPhoneNumber);
        then(uLibrarianRepository).should().save(any(ULibrarian.class));
        then(uLibrarianMapper).should().mapEntityToDto(savedULibrarian);

        assertThat(responseULibrarianDTO)
                .isNotNull()
                .isEqualToComparingFieldByField(savedULibrarianDTO);

        assertThat(responseULibrarianDTO.getFirstName()).isEqualTo(firstName);
        assertThat(responseULibrarianDTO.getLastName()).isEqualTo(lastName);
        assertThat(responseULibrarianDTO.getUserPhoneNumber()).isEqualTo(userPhoneNumber);
        assertThat(responseULibrarianDTO.getUserEmail()).isEqualTo(userEmail);
        assertThat(responseULibrarianDTO.getPassword()).isEqualTo(encodedPassword);
        assertThat(responseULibrarianDTO.getAuthoritiesDTO()).contains(librarianRoleDTO, managerRoleDTO);
    }

    @Test
    void itShouldSignUpNewLibrarianWithUniqueDetails() {
        // Given...
        String firstName = "firstName";
        String lastName = "lastName";
        String userPhoneNumber = "userPhoneNumber";
        String userEmail = "userEmail";
        String password = "password";
        String encodedPassword = "encoded_password";

        Set<Role> authorities = new HashSet<>();
        Role librarianRole = new Role("LIBRARIAN");
        authorities.add(librarianRole);

        Set<RoleDTO> authoritiesDTO = new HashSet<>();
        RoleDTO librarianRoleDTO = new RoleDTO("LIBRARIAN");
        authoritiesDTO.add(librarianRoleDTO);

        ULibrarianDTO uLibrarianDTORequest
                = new ULibrarianDTO(firstName, lastName, userPhoneNumber, userEmail, password);

        ULibrarian mappedULibrarian
                = new ULibrarian(firstName, lastName, userPhoneNumber, userEmail, password);

        ULibrarian savedULibrarian
                = new ULibrarian(firstName, lastName, userPhoneNumber, userEmail, encodedPassword, authorities);

        ULibrarianDTO savedULibrarianDTO
                = new ULibrarianDTO(firstName, lastName, userPhoneNumber, userEmail, encodedPassword, authoritiesDTO);

        given(uLibrarianMapper.mapDtoToEntity(uLibrarianDTORequest)).willReturn(mappedULibrarian);
        given(roleRepository.findByAuthority(librarianRole.getAuthority())).willReturn(Optional.of(librarianRole));
        given(passwordEncoder.encode(password)).willReturn(encodedPassword);
        given(uLibrarianRepository.findByUserEmail(userEmail)).willReturn(Optional.empty());
        given(uLibrarianRepository.findByUserPhoneNumber(userPhoneNumber)).willReturn(Optional.empty());
        given(uLibrarianRepository.save(any(ULibrarian.class))).willReturn(savedULibrarian);
        given(uLibrarianMapper.mapEntityToDto(any(ULibrarian.class))).willReturn(savedULibrarianDTO);

        // When
        ULibrarianDTO responseULibrarianDTO = underTest.signUpLibraryManager(uLibrarianDTORequest);

        // Then

        then(uLibrarianMapper).should().mapDtoToEntity(uLibrarianDTORequest);
        then(roleRepository).should().findByAuthority(librarianRole.getAuthority());
        then(passwordEncoder).should().encode(password);
        then(uLibrarianRepository).should().findByUserEmail(userEmail);
        then(uLibrarianRepository).should().findByUserPhoneNumber(userPhoneNumber);
        then(uLibrarianRepository).should().save(any(ULibrarian.class));
        then(uLibrarianMapper).should().mapEntityToDto(savedULibrarian);

        assertThat(responseULibrarianDTO)
                .isNotNull()
                .isEqualToComparingFieldByField(savedULibrarianDTO);

        assertThat(responseULibrarianDTO).isNotNull();
        assertThat(responseULibrarianDTO.getFirstName()).isEqualTo(firstName);
        assertThat(responseULibrarianDTO.getLastName()).isEqualTo(lastName);
        assertThat(responseULibrarianDTO.getUserPhoneNumber()).isEqualTo(userPhoneNumber);
        assertThat(responseULibrarianDTO.getUserEmail()).isEqualTo(userEmail);
        assertThat(responseULibrarianDTO.getPassword()).isEqualTo(encodedPassword);
        assertThat(responseULibrarianDTO.getAuthoritiesDTO()).containsExactly(librarianRoleDTO);
    }

    @Test
    void itShouldNotSignUpLibraryManagerWithExistingUserEmail() {
        // Given...
        String firstName = "firstName";
        String lastName = "lastName";
        String userPhoneNumber = "userPhoneNumber";
        String userEmail = "userEmail";
        String password = "password";
        String encodedPassword = "encoded_password";

        Set<Role> authorities = new HashSet<>();
        Role librarianRole = new Role("LIBRARIAN");
        Role managerRole = new Role("MANAGER");
        authorities.add(librarianRole);
        authorities.add(managerRole);

        Set<RoleDTO> authoritiesDTO = new HashSet<>();
        RoleDTO librarianRoleDTO = new RoleDTO("LIBRARIAN");
        RoleDTO managerRoleDTO = new RoleDTO("MANAGER");
        authoritiesDTO.add(librarianRoleDTO);
        authoritiesDTO.add(managerRoleDTO);

        ULibrarianDTO uLibrarianDTORequest
                = new ULibrarianDTO(firstName, lastName, userPhoneNumber, userEmail, password);

        ULibrarian mappedULibrarian
                = new ULibrarian(firstName, lastName, userPhoneNumber, userEmail, password);

        ULibrarian foundLibrarian
                = new ULibrarian(firstName, lastName, userPhoneNumber, userEmail, encodedPassword, authorities);

        given(uLibrarianMapper.mapDtoToEntity(uLibrarianDTORequest)).willReturn(mappedULibrarian);
        given(roleRepository.findByAuthority(librarianRole.getAuthority())).willReturn(Optional.of(librarianRole));
        given(roleRepository.findByAuthority(managerRole.getAuthority())).willReturn(Optional.of(managerRole));
        given(passwordEncoder.encode(password)).willReturn(encodedPassword);
        given(uLibrarianRepository.findByUserEmail(userEmail)).willReturn(Optional.of(foundLibrarian));

        // When
        ULibrarianDTO responseULibrarianDTO;
        try {
            responseULibrarianDTO = underTest.signUpLibraryManager(uLibrarianDTORequest);
        } catch (IllegalStateException e) {
            responseULibrarianDTO = null;
        }

        // Then
            // Verify that the necessary methods were called with the correct arguments
        then(uLibrarianMapper).should().mapDtoToEntity(uLibrarianDTORequest);
        then(roleRepository).should().findByAuthority(librarianRole.getAuthority());
        then(passwordEncoder).should().encode(password);
        then(uLibrarianRepository).should().findByUserEmail(userEmail);
        then(uLibrarianRepository).shouldHaveNoMoreInteractions();
        then(uLibrarianMapper).should(never()).mapEntityToDto(any());

        assertThat(responseULibrarianDTO)
                .isNull();

        assertThatThrownBy(() -> underTest.signUpLibraryManager(uLibrarianDTORequest))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(
                        String.format("Email '[%s]' alredy taken", foundLibrarian.getUserEmail())
                );
    }

    @Test
    void itShouldNotSignUpLibraryManagerWithExistingPhoneNumber() {
        // Given...
        String firstName = "firstName";
        String lastName = "lastName";
        String userPhoneNumber = "userPhoneNumber";
        String userEmail = "userEmail";
        String password = "password";
        String encodedPassword = "encoded_password";

        Set<Role> authorities = new HashSet<>();
        Role librarianRole = new Role("LIBRARIAN");
        Role managerRole = new Role("MANAGER");
        authorities.add(librarianRole);
        authorities.add(managerRole);

        Set<RoleDTO> authoritiesDTO = new HashSet<>();
        RoleDTO librarianRoleDTO = new RoleDTO("LIBRARIAN");
        RoleDTO managerRoleDTO = new RoleDTO("MANAGER");
        authoritiesDTO.add(librarianRoleDTO);
        authoritiesDTO.add(managerRoleDTO);

        ULibrarianDTO uLibrarianDTORequest
                = new ULibrarianDTO(firstName, lastName, userPhoneNumber, userEmail, password);

        ULibrarian mappedULibrarian
                = new ULibrarian(firstName, lastName, userPhoneNumber, userEmail, password);

        ULibrarian foundLibrarian
                = new ULibrarian(firstName, lastName, userPhoneNumber, userEmail, encodedPassword, authorities);

        given(uLibrarianMapper.mapDtoToEntity(uLibrarianDTORequest)).willReturn(mappedULibrarian);
        given(roleRepository.findByAuthority(librarianRole.getAuthority())).willReturn(Optional.of(librarianRole));
        given(roleRepository.findByAuthority(managerRole.getAuthority())).willReturn(Optional.of(managerRole));
        given(passwordEncoder.encode(password)).willReturn(encodedPassword);
        given(uLibrarianRepository.findByUserEmail(userEmail)).willReturn(Optional.empty());
        given(uLibrarianRepository.findByUserPhoneNumber(userPhoneNumber)).willReturn(Optional.of(foundLibrarian));

        // When
        ULibrarianDTO responseULibrarianDTO;
        try {
            responseULibrarianDTO = underTest.signUpLibraryManager(uLibrarianDTORequest);
        } catch (IllegalStateException e) {
            responseULibrarianDTO = null;
        }

        // Then
        verify(uLibrarianMapper).mapDtoToEntity(uLibrarianDTORequest);
        verify(roleRepository).findByAuthority(librarianRole.getAuthority());
        verify(roleRepository).findByAuthority(managerRole.getAuthority());
        verify(passwordEncoder).encode(password);
        verify(uLibrarianRepository).findByUserEmail(userEmail);
        verify(uLibrarianRepository).findByUserPhoneNumber(userPhoneNumber);
        verify(uLibrarianRepository, never()).save(any(ULibrarian.class));
        verify(uLibrarianMapper, never()).mapEntityToDto(any(ULibrarian.class));

        assertThat(responseULibrarianDTO)
                .isNull();

        assertThatThrownBy(() -> underTest.signUpLibraryManager(uLibrarianDTORequest))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(
                        String.format("Phone number '[%s]' alredy taken", foundLibrarian.getUserPhoneNumber())
                );
    }
}