package com.miguel.biblioteca.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Addresses")
public class LibraryAddress {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    private Integer idLibraryAddress;
    private String libraryAdressName;
    private String libraryAdressNumber;
    private String libraryLocality;
    private String libraryProvince;
    private String libraryPostalCode;
}
