package com.miguel.library.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTOSearchReaderRequest {

    private String readerName;

    private String readerNumber;

    private String email;

    private String phoneNumber;

    private Integer minBirthYear;

    private Integer maxBirthYear;

    private Character gender;
}
