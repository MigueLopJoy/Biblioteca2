package com.miguel.biblioteca.services;

import com.miguel.biblioteca.DTO.LibraryDTO;
import com.miguel.biblioteca.DTO.RoleDTO;
import com.miguel.biblioteca.DTO.ULibrarianDTO;
import com.miguel.biblioteca.mapper.LibraryMapper;
import com.miguel.biblioteca.mapper.ULibrarianMapper;
import com.miguel.biblioteca.model.Library;
import com.miguel.biblioteca.DTO.LibraryRegistrationRequestDTO;
import com.miguel.biblioteca.model.Role;
import com.miguel.biblioteca.model.ULibrarian;
import com.miguel.biblioteca.repositories.ILibraryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class ImpAuthenticationServiceTest {

    @Mock
    private ILibraryRepository libraryRepository;

    @Mock
    private IULibrarianService uLibrarianService;

    @Mock
    private LibraryMapper libraryMapper;

    @Mock
    private ULibrarianMapper uLibrarianMapper;

    @InjectMocks
    private ImpAuthenticationService underTest;

    // Library attributes
    String libraryName;
    String libraryPhoneNumber;
    String libraryEmail;
    String libraryAddress;
    String city;
    String province;
    String postalCode;

    // Librarian attributes
    String firstName;
    String lastName;
    String userPhoneNumber;
    String userEmail;
    String password;
    Role managerRole;
    Role librarianRole;
    Set<Role> authorities;
    RoleDTO managerRoleDTO;
    RoleDTO librarianRoleDTO;
    Set<RoleDTO> authoritiesDTO;

    // Library and Librarian required objects
    LibraryDTO libraryDTO;
    ULibrarianDTO libraryManagerDTO;
    LibraryRegistrationRequestDTO request;
    ULibrarianDTO savedLibraryManagerDTO;
    ULibrarian savedLibraryManagerMapped;
    Library savedLibrary;
    LibraryDTO savedLibraryDTO;
    LibraryDTO responseLibraryDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        libraryName = "Library";
        libraryPhoneNumber = "6666";
        libraryEmail = "library@email.com";
        libraryAddress = "LibraryAddress 1234";
        city = "City";
        province = "Province";
        postalCode = "1234";

        firstName = "firstName";
        lastName  = "lastName";
        userPhoneNumber = "666666666";
        userEmail = "example@mail.com";
        password = "password";

        authoritiesDTO = new HashSet<>();
        managerRoleDTO = new RoleDTO("MANAGER");
        librarianRoleDTO = new RoleDTO("LIBRARIAN");

        authorities = new HashSet<>();
        managerRole = new Role("MANAGER");
        librarianRole = new Role("LIBRARIAN");
    }

    @Test
    void itShouldSignUpNewLibrary() {
        // Given

            // New library data
        libraryDTO
                = new LibraryDTO(libraryName, libraryPhoneNumber, libraryEmail, libraryAddress, city, province, postalCode);

            // New library Manager data
        libraryManagerDTO = new ULibrarianDTO(firstName, lastName, userPhoneNumber, userEmail, password);

            // A new library registration request
        request = new LibraryRegistrationRequestDTO(libraryDTO, libraryManagerDTO);

            // New library mapped
        Library libraryMapped
                = new Library(libraryName, libraryPhoneNumber, libraryEmail, libraryAddress, city, province, postalCode);

            // New library manager data with its authorities returned from its service after stored in database
        authoritiesDTO.add(librarianRoleDTO);
        authoritiesDTO.add(managerRoleDTO);
        savedLibraryManagerDTO
                = new ULibrarianDTO(firstName, lastName, userPhoneNumber, userEmail, password, authoritiesDTO);

            // New library manager mapped with its authorities
        authorities.add(librarianRole);
        authorities.add(managerRole);
        savedLibraryManagerMapped = new ULibrarian(firstName, lastName, userPhoneNumber, userEmail, password, authorities);

            // New library with its librarians -the library manager - associated
        List<ULibrarian> currentLibrarians = new ArrayList<>();
        currentLibrarians.add(savedLibraryManagerMapped);
        savedLibrary
                = new Library(libraryName, libraryPhoneNumber, libraryEmail, libraryAddress, city, province, postalCode, currentLibrarians);

            // New library data with its current librarians' data -the data of the library manager - associated ready to be returned
        List<ULibrarianDTO> currentLibrariansDTO = new ArrayList<>();
        currentLibrariansDTO.add(savedLibraryManagerDTO);
        savedLibraryDTO = new LibraryDTO(libraryName, libraryPhoneNumber, libraryEmail, libraryAddress, city, province, postalCode, currentLibrariansDTO);

        given(libraryMapper.mapDtoToEntity(libraryDTO)).willReturn(libraryMapped);
        given(uLibrarianService.signUpLibraryManager(libraryManagerDTO)).willReturn(savedLibraryManagerDTO);
        given(uLibrarianMapper.mapDtoToEntity(savedLibraryManagerDTO)).willReturn(savedLibraryManagerMapped);
        given(libraryRepository.save(any(Library.class))).willReturn(savedLibrary);
        given(libraryMapper.mapEntityToDto(savedLibrary)).willReturn(savedLibraryDTO);

        // When
        try {
            responseLibraryDTO = underTest.SignUpNewLibrary(request);
        } catch (Exception e) {
            responseLibraryDTO = null;
        }

        // Then
            // Verify that the necessary methods were called with the correct arguments
        verify(libraryMapper).mapDtoToEntity(libraryDTO);
        verify(uLibrarianService).signUpLibraryManager(libraryManagerDTO);
        verify(uLibrarianMapper).mapDtoToEntity(savedLibraryManagerDTO);
        verify(libraryRepository).save(any(Library.class));
        verify(libraryMapper).mapEntityToDto(savedLibrary);

        assertThat(responseLibraryDTO)
                .isNotNull()
                .isEqualToComparingFieldByField(savedLibraryDTO);

        assertThat(responseLibraryDTO.getLibraryName()).isEqualTo(libraryName);
        assertThat(responseLibraryDTO.getLibraryAddress()).isEqualTo(libraryAddress);
        assertThat(responseLibraryDTO.getCity()).isEqualTo(city);
        assertThat(responseLibraryDTO.getProvince()).isEqualTo(province);
        assertThat(responseLibraryDTO.getPostalCode()).isEqualTo(postalCode);
        assertThat(responseLibraryDTO.getLibrariansDTO()).isEqualTo(currentLibrariansDTO);
    }

    @Test
    void itShouldNotSignUpNewLibraryWhenSomeLibraryAttributeIsNull() {
        // Given
            // New library data
        libraryDTO = new LibraryDTO(libraryName, libraryPhoneNumber, libraryEmail, libraryAddress, city, province, null);

            // New library Manager data
        libraryManagerDTO = new ULibrarianDTO(firstName, lastName, userPhoneNumber, userEmail, password);

            // A new library registration request
        request = new LibraryRegistrationRequestDTO(libraryDTO, libraryManagerDTO);

            // New library mapped
        Library libraryMapped
                = new Library(libraryName, libraryPhoneNumber, libraryEmail, libraryAddress, city, province, null);

            // New library manager data with its authorities returned from its service after stored in database
        authoritiesDTO.add(librarianRoleDTO);
        authoritiesDTO.add(managerRoleDTO);
        savedLibraryManagerDTO = new ULibrarianDTO(firstName, lastName, userPhoneNumber, userEmail, password, authoritiesDTO);

            // New library manager mapped with its authorities
        authorities.add(librarianRole);
        authorities.add(managerRole);
        savedLibraryManagerMapped = new ULibrarian(firstName, lastName, userPhoneNumber, userEmail, password, authorities);

        given(libraryMapper.mapDtoToEntity(libraryDTO)).willReturn(libraryMapped);
        given(uLibrarianService.signUpLibraryManager(libraryManagerDTO)).willReturn(savedLibraryManagerDTO);
        given(uLibrarianMapper.mapDtoToEntity(savedLibraryManagerDTO)).willReturn(savedLibraryManagerMapped);
        given(libraryRepository.save(any(Library.class)))
                .willThrow(new DataIntegrityViolationException(
                        "not-null property references a null or transient value : com.miguel.biblioteca.model.Library.postalCode"
                ));

        // When
        try {
            responseLibraryDTO = underTest.SignUpNewLibrary(request);
        } catch (DataIntegrityViolationException e) {
            responseLibraryDTO = null;
        }

        // Then
            // Verify that the necessary methods were called with the correct arguments
        verify(libraryMapper).mapDtoToEntity(libraryDTO);
        verify(uLibrarianService).signUpLibraryManager(libraryManagerDTO);
        verify(uLibrarianMapper).mapDtoToEntity(savedLibraryManagerDTO);
        verify(libraryRepository).save(any(Library.class));
        verify(libraryMapper, never()).mapEntityToDto(savedLibrary);

        assertThat(responseLibraryDTO).isNull();

        assertThatThrownBy(() -> underTest.SignUpNewLibrary(request))
                .hasMessageContaining(
                        "not-null property references a null or transient value : com.miguel.biblioteca.model.Library.postalCode"
                )
                .isInstanceOf(DataIntegrityViolationException.class);

            // finally
        then(libraryMapper).should(never()).mapEntityToDto(any(Library.class));

    }

    @Test
    void itShouldNotSignUpNewLibraryWhenSomeLibraryManagerAttributeIsNull() {
        // Given
            // New library data
        libraryDTO =
                new LibraryDTO(libraryName, libraryPhoneNumber, libraryEmail, libraryAddress, city, province, postalCode);

            // New library Manager data
        libraryManagerDTO =
                new ULibrarianDTO(firstName, lastName, userPhoneNumber, userEmail, null);

            // A new library registration request
        request =
                new LibraryRegistrationRequestDTO(libraryDTO, libraryManagerDTO);

            // New library mapped
        Library libraryMapped =
                new Library(libraryName, libraryPhoneNumber, libraryEmail, libraryAddress, city, province, postalCode);

        given(libraryMapper.mapDtoToEntity(libraryDTO)).willReturn(libraryMapped);
        given(uLibrarianService.signUpLibraryManager(libraryManagerDTO))
                .willThrow(new DataIntegrityViolationException(
                        "not-null property references a null or transient value : com.miguel.biblioteca.model.ULibrarian.password"
                ));

        // When
        LibraryDTO responseLibraryDTO;
        try {
            responseLibraryDTO = underTest.SignUpNewLibrary(request);
        } catch (DataIntegrityViolationException e) {
            responseLibraryDTO = null;
        }

        // Then
        then(libraryMapper).should().mapDtoToEntity(libraryDTO);
        then(uLibrarianService).should().signUpLibraryManager(libraryManagerDTO);
        then(uLibrarianMapper).should(never()).mapDtoToEntity(any());
        then(libraryRepository).should(never()).save(any());
        then(libraryMapper).should(never()).mapEntityToDto(any());

        assertThat(responseLibraryDTO).isNull();

        assertThatThrownBy(() -> underTest.SignUpNewLibrary(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(
                        "not-null property references a null or transient value : com.miguel.biblioteca.model.ULibrarian.password"
                );
    }

    @Test
    void itShouldNotSignUpNewLibraryWhenLibrayManagerExists() {
        // Given

            // New library data
        libraryDTO =
                new LibraryDTO(libraryName, libraryPhoneNumber, libraryEmail, libraryAddress, city, province, postalCode);

            // New library Manager data
        libraryManagerDTO =
                new ULibrarianDTO(firstName, lastName, userPhoneNumber, userEmail, password);

            // A new library registration request
        request =
                new LibraryRegistrationRequestDTO(libraryDTO, libraryManagerDTO);

            // New library mapped
        Library libraryMapped =
                new Library(libraryName, libraryPhoneNumber, libraryEmail, libraryAddress, city, province, postalCode);

            // New library manager data with its authorities returned from its service after stored in database
        authoritiesDTO.add(librarianRoleDTO);
        authoritiesDTO.add(managerRoleDTO);
        savedLibraryManagerDTO = new ULibrarianDTO(firstName, lastName, userPhoneNumber, userEmail, password, authoritiesDTO);

            // New library manager mapped with its authorities
        authorities.add(librarianRole);
        authorities.add(managerRole);
        savedLibraryManagerMapped = new ULibrarian(firstName, lastName, userPhoneNumber, userEmail, password, authorities);

            // New library with its librarians -the library manager - associated
        List<ULibrarian> currentLibrarians = new ArrayList<>();
        currentLibrarians.add(savedLibraryManagerMapped);
        savedLibrary =
                new Library(libraryName, libraryPhoneNumber, libraryEmail, libraryAddress, city, province, postalCode, currentLibrarians);

            // New library data with its current librarians' data -the data of the library manager - associated ready to be returned
        List<ULibrarianDTO> currentLibrariansDTO = new ArrayList<>();
        currentLibrariansDTO.add(savedLibraryManagerDTO);
        savedLibraryDTO =
                new LibraryDTO(libraryName, libraryPhoneNumber, libraryEmail, libraryAddress, city, province, postalCode, currentLibrariansDTO);

        given(libraryMapper.mapDtoToEntity(libraryDTO)).willReturn(libraryMapped);
        given(uLibrarianService.signUpLibraryManager(libraryManagerDTO)).willReturn(savedLibraryManagerDTO);
        given(uLibrarianMapper.mapDtoToEntity(savedLibraryManagerDTO)).willReturn(savedLibraryManagerMapped);
        given(libraryRepository.save(any(Library.class))).willReturn(savedLibrary);
        given(libraryMapper.mapEntityToDto(savedLibrary)).willReturn(savedLibraryDTO);

        // When
        try {
            responseLibraryDTO = underTest.SignUpNewLibrary(request);
        } catch (Exception e) {
            responseLibraryDTO = null;
        }

        // Then
        // Verify that the necessary methods were called with the correct arguments
        verify(libraryMapper).mapDtoToEntity(libraryDTO);
        verify(uLibrarianService).signUpLibraryManager(libraryManagerDTO);
        verify(uLibrarianMapper).mapDtoToEntity(savedLibraryManagerDTO);
        verify(libraryRepository).save(any(Library.class));
        verify(libraryMapper).mapEntityToDto(savedLibrary);

        assertThat(responseLibraryDTO)
                .isNotNull()
                .isEqualToComparingFieldByField(savedLibraryDTO);

        assertThat(responseLibraryDTO.getLibraryName()).isEqualTo(libraryName);
        assertThat(responseLibraryDTO.getLibraryAddress()).isEqualTo(libraryAddress);
        assertThat(responseLibraryDTO.getCity()).isEqualTo(city);
        assertThat(responseLibraryDTO.getProvince()).isEqualTo(province);
        assertThat(responseLibraryDTO.getPostalCode()).isEqualTo(postalCode);
        assertThat(responseLibraryDTO.getLibrariansDTO()).isEqualTo(currentLibrariansDTO);
    }
}