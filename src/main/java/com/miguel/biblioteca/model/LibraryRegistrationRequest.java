package com.miguel.biblioteca.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.miguel.biblioteca.DTO.LibraryDTO;
import com.miguel.biblioteca.DTO.ULibrarianDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@Setter
public class LibraryRegistrationRequest {

    private LibraryDTO libraryDTO;

    public LibraryRegistrationRequest(
            @JsonProperty("libraryDTO") LibraryDTO libraryDTO
    ) {
        this.libraryDTO = libraryDTO;
    }
}
