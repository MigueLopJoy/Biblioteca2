package com.miguel.biblioteca.services;

import com.miguel.biblioteca.DTO.AuthenticationRequestDTO;
import com.miguel.biblioteca.DTO.AuthenticationResponseDTO;
import com.miguel.biblioteca.DTO.LibraryDTO;
import com.miguel.biblioteca.DTO.LibraryRegistrationRequestDTO;

public interface IAuthenticationService {
    public LibraryDTO SignUpNewLibrary(LibraryRegistrationRequestDTO request);
    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO request);
}
