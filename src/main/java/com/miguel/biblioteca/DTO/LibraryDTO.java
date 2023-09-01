package com.miguel.biblioteca.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class LibraryDTO {
    private String libraryName;
    private String libraryPhoneNumber;
    private String libraryEmail;
    private String libraryAddress;
    private String city;
    private String province;
    private String postalCode;
    private List<ULibrarianDTO> librariansDTO;

    public LibraryDTO(String libraryName,
                      String libraryPhoneNumber,
                      String libraryEmail,
                      String libraryAddress,
                      String city,
                      String province,
                      String postalCode) {
        this.libraryName = libraryName;
        this.libraryPhoneNumber = libraryPhoneNumber;
        this.libraryEmail = libraryEmail;
        this.libraryAddress = libraryAddress;
        this.city = city;
        this.province = province;
        this.postalCode = postalCode;
    }
}
