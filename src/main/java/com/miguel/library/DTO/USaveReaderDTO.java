package com.miguel.library.DTO;

import com.miguel.library.Validations.UniqueEmail;
import com.miguel.library.Validations.UniquePhoneNumber;
import com.miguel.library.Validations.YearNotGreaterThanCurrent;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@UniquePhoneNumber
@UniqueEmail
@AllArgsConstructor
public class USaveReaderDTO {
    @Pattern(regexp = "^[a-zA-ZÁÉÍÓÚáéíóúÑñ]+(\\s[A-Za-zÁÉÍÓÚáéíóúÑñ]+)*$", message = "Must introduce a valid first name")
    private String firstName;

    @Pattern(regexp = "^[a-zA-ZÁÉÍÓÚáéíóúÑñ]+(\\s[A-Za-zÁÉÍÓÚáéíóúÑñ]+)*$", message = "Must introduce a valid last name")
    private String lastName;

    @NotNull(message = "Date of birth required")
    @YearNotGreaterThanCurrent(message = "Edition year should not be greater than current year")
    @Min(value = 1900, message = "Edition year should not be under 1900")
    private Integer yearOfBirth;

    @NotNull(message = "Must select a gender")
    private Character gender;

    @Email(message = "Must provide a valid email")
    private String email;

    @Pattern(regexp = "^\\+?\\d{1,3}\\d{1,14}$", message = "Must provide a valid phone number")
    private String phoneNumber;

}
