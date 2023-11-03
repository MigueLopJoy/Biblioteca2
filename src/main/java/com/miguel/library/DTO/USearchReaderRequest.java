package com.miguel.library.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class USearchReaderRequest {

    private String readerName;

    private String readerNumber;

    private String email;

    private String phoneNumber;

    private Integer minBirthYear;

    private Integer maxBirthYear;

    private Character gender;
}
