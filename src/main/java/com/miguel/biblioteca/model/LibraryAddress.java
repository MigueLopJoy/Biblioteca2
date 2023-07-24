package com.miguel.biblioteca.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Getter @Setter
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "addresses")
public class LibraryAddress {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    private Integer idLibraryAddress;

    @NonNull
    private String libraryAdressName;

    @NonNull
    private Integer libraryAdressNumber;

    @NonNull
    private String libraryLocality;

    @NonNull
    private String libraryProvince;

    @NonNull
    private String libraryPostalCode;
}
