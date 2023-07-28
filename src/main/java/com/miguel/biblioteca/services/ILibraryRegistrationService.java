package com.miguel.biblioteca.services;

import com.miguel.biblioteca.DTO.LibraryDTO;
import com.miguel.biblioteca.DTO.ULibrarianDTO;
import com.miguel.biblioteca.model.ULibrarian;

public interface ILibraryRegistrationService {
    public LibraryDTO SignUpNewLibrary(LibraryDTO libraryDTO, ULibrarianDTO uLibrarianDTO);
}
