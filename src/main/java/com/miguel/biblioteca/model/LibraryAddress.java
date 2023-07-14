package com.miguel.biblioteca.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class LibraryAddress {
    private String libraryAdressName;
    private String libraryAdressNumber;
    private String libraryLocality;
    private String libraryProvince;
    private String libraryPostalCode;
}
