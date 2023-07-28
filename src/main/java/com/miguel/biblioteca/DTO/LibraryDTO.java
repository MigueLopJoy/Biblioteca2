package com.miguel.biblioteca.DTO;

import java.util.ArrayList;
import java.util.List;

import com.miguel.biblioteca.model.ULibrarian;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class LibraryDTO {
    private String libraryName;
    private String libraryAddress;
    private String city;
    private String postalCode;
    private List<ULibrarianDTO> librariansDTO;

    public LibraryDTO(String libraryName,
                      String libraryAddress,
                      String city,
                      String postalCode) {
        this.libraryName = libraryName;
        this.libraryAddress = libraryAddress;
        this.city = city;
        this.postalCode = postalCode;
    }
}
