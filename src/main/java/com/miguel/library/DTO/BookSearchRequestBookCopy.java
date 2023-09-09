package com.miguel.library.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class BookSearchRequestBookCopy extends BookSearchRequestBookEdition {

    private String barCode;

    private Long minRegistrationNumber;

    private Long maxRegistrationNumber;

    private Date minRegistrationDate;

    private Date maxRegistrationDate;

    private String signature;

}
