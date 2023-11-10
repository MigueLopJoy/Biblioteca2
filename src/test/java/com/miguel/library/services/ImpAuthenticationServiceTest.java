package com.miguel.library.services;

import com.miguel.library.DTO.*;
import com.miguel.library.model.Library;
import com.miguel.library.model.Role;
import com.miguel.library.model.ULibrarian;
import com.miguel.library.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ImpAuthenticationServiceTest {
    @Mock
    private ImpLibraryService libraryService;

    @Mock
    private ImpULibrarianService librarianService;

    @Mock
    private ImpTokenService tokenService;

    @InjectMocks
    private ImpAuthenticationService authService;

    @Test
    void itShouldRegister() {
        // Given
        AuthRegisterRequest registerRequest = createSampleAuthRegisterRequest();
        Library libraryToSave = createSampleLibrary();
        ULibrarian savedLibrarian = createSampleSavedLibrarian();
        String jwtToken = "sampleJwtToken";
        String refreshToken = "sampleRefreshToken";

        when(libraryService.createLibraryFromDTO(any())).thenReturn(libraryToSave);
        when(librarianService.createLibrarianFromDTO(any())).thenReturn(savedLibrarian);
        when(libraryService.saveNewLibrary(any())).thenReturn(new LibraryResponseDTO("Library saved", libraryToSave));
        when(librarianService.saveNewLibrarian(any())).thenReturn(new UserDTOLibrarianResponse("Librarian saved", savedLibrarian));
        when(tokenService.generateToken(any())).thenReturn(jwtToken);
        when(tokenService.generateRefreshToken(any())).thenReturn(refreshToken);

        // When

        AuthRegisterResponse authResponse = authService.register(registerRequest);

        // Then

        verify(libraryService, times(1)).createLibraryFromDTO(registerRequest.getLibrary());
        verify(libraryService, times(1)).saveNewLibrary(libraryToSave);
        verify(librarianService, times(1)).createLibrarianFromDTO(registerRequest.getLibrarian(), libraryToSave);
        verify(librarianService, times(1)).saveNewLibrarian(savedLibrarian);
        verify(tokenService, times(1)).generateToken(savedLibrarian);
        verify(tokenService, times(1)).generateRefreshToken(savedLibrarian);
        verify(tokenService, times(1)).saveToken(
                tokenService.generateUserTokenFromJwtString(jwtToken, new User())
        );
    }
    private AuthRegisterRequest createSampleAuthRegisterRequest() {
        AuthRegisterRequest request = new AuthRegisterRequest();
        request.setLibrary(createSampleLibraryDTO());
        request.setLibrarian(createSampleLibrarianDTO());
        return request;
    }

    private LibraryDTOSaveLibrary createSampleLibraryDTO() {
        LibraryDTOSaveLibrary libraryDTO = new LibraryDTOSaveLibrary();
        libraryDTO.setLibraryName("Example Library");
        libraryDTO.setLibraryPhoneNumber("+1234567890");
        libraryDTO.setLibraryEmail("library@example.com");
        libraryDTO.setLibraryAddress("123 Main Street");
        libraryDTO.setCity("Cityville");
        libraryDTO.setProvince("Provinceland");
        libraryDTO.setPostalCode("12345-6789");
        return libraryDTO;
    }

    private Library createSampleLibrary() {
        Library library = new Library();
        library.setLibraryName("Example Library");
        library.setLibraryPhoneNumber("+1234567890");
        library.setLibraryEmail("library@example.com");
        library.setLibraryAddress("123 Main Street");
        library.setCity("Cityville");
        library.setProvince("Provinceland");
        library.setPostalCode("12345-6789");
        return library;
    }

    private UserDTOSaveLibrarian createSampleLibrarianDTO() {
        UserDTOSaveLibrarian userDTOSaveLibrarian = new UserDTOSaveLibrarian();
        userDTOSaveLibrarian.setFirstName("John");
        userDTOSaveLibrarian.setLastName("Doe");
        userDTOSaveLibrarian.setGender('M');
        userDTOSaveLibrarian.setBirthYear(1990);
        userDTOSaveLibrarian.setPhoneNumber("+9876543210");
        userDTOSaveLibrarian.setEmail("john.doe@example.com");
        userDTOSaveLibrarian.setPassword("P@ssw0rd");
        return userDTOSaveLibrarian;
    }

    private ULibrarian createSampleSavedLibrarian() {
            ULibrarian uLibrarian = new ULibrarian();
            uLibrarian.setFirstName("John");
            uLibrarian.setLastName("Doe");
            uLibrarian.setGender('M');
            uLibrarian.setBirthYear(1990);
            uLibrarian.setPhoneNumber("+9876543210");
            uLibrarian.setEmail("john.doe@example.com");
            uLibrarian.setPassword("P@ssw0rd");
            uLibrarian.setRole(Role.LIBRARIAN);
            return uLibrarian;
    }
}