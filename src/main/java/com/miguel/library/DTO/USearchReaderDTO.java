package com.miguel.library.DTO;

import com.miguel.library.Validations.UniqueEmailOrPhoneNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class USearchReaderDTO {

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private String readerNumber;

    private LocalDate dateOfBirth;

    private Character gender;
}
