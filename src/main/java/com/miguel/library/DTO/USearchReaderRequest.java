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

    private String email;

    private String phoneNumber;

    private String readerNumber;

    private LocalDate minDateOfBirth;

    private LocalDate maxDateOfBirth;

    private Character gender;
}
