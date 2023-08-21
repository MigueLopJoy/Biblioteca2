package com.miguel.biblioteca.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.miguel.biblioteca.DTO.LibraryDTO;
import com.miguel.biblioteca.DTO.ULibrarianDTO;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter @Setter
public class LibraryRegistrationRequestDTO {

    @NonNull
    private LibraryDTO libraryDTO;

    @NonNull
    private ULibrarianDTO uLibrarianDTO;

    public LibraryRegistrationRequestDTO(
            @JsonProperty("libraryDTO") LibraryDTO libraryDTO,
            @JsonProperty("libraryManagerDTO") ULibrarianDTO uLibrarianDTO
    ) {
        this.libraryDTO = libraryDTO;
        this.uLibrarianDTO = uLibrarianDTO;
    }
}
