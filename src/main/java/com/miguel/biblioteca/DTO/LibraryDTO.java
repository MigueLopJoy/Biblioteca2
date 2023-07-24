package com.miguel.biblioteca.DTO;

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

    @NotBlank
    private String libraryName;
    private LibraryAddressDTO libraryAddressDTO;
    private ULibrarianDTO libraryManagerDTO;
}
