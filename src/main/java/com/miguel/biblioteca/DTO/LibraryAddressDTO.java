package com.miguel.biblioteca.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class LibraryAddressDTO {
    private String libraryAdressName;
    private String libraryAdressNumber;
    private String libraryLocality;
    private String libraryProvince;
    private String libraryPostalCode;
}
