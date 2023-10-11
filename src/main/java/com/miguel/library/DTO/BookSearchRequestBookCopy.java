package com.miguel.library.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class BookSearchRequestBookCopy extends BookSearchRequestBookEdition {

    private String barCode;

    private Long minRegistrationNumber;

    private Long maxRegistrationNumber;

    private LocalDate minRegistrationDate;

    private LocalDate maxRegistrationDate;

    private String signature;

    private Character status;

    private Boolean borrowed;
}
