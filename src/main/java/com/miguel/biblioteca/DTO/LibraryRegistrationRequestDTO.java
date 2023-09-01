package com.miguel.biblioteca.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
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
            @JsonProperty("library") LibraryDTO libraryDTO,
            @JsonProperty("libraryManager") ULibrarianDTO uLibrarianDTO
    ) {
        this.libraryDTO = libraryDTO;
        this.uLibrarianDTO = uLibrarianDTO;
    }
}
