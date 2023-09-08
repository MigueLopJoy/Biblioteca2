package com.miguel.library.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class BookSearchRequest {
    private String title;

    private String author;

    private String editor;

    private String language;

    private String ISBN;

    private String barCode;

    private Long minRegistrationNumber;

    private Long maxRegistrationNumber;

    private Date minRegistrationDate;

    private Date maxRegistrationDate;

    private String signature;

}
