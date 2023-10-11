package com.miguel.library.DTO;

import com.miguel.library.Validations.UniqueEmailOrPhoneNumber;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class USaveReaderDTO {
    @Pattern(regexp = "^[a-zA-ZÁÉÍÓÚáéíóúÑñ]+(\\s[A-Za-zÁÉÍÓÚáéíóúÑñ]+)*$", message = "Must introduce a valid first name")
    private String firstName;

    @Pattern(regexp = "^[a-zA-ZÁÉÍÓÚáéíóúÑñ]+(\\s[A-Za-zÁÉÍÓÚáéíóúÑñ]+)*$", message = "Must introduce a valid last name")
    private String lastName;
    @NotNull(message = "Date of birth required")
    @Past(message = "Dade of birth should not be earlier than current date")
    private LocalDate dateOfBirth;

    @NotNull(message = "Must select a gender")
    @Pattern(regexp = "^[MF]$", message = "Must provide a valid gender")
    private Character gender;

    @Email(message = "Must provide a valid email")
    @UniqueEmailOrPhoneNumber(message = "Email already taken")
    private String email;

    @Pattern(regexp = "^\\+?\\d{1,3}\\d{1,14}$", message = "Must provide a valid phone number")
    @UniqueEmailOrPhoneNumber(message = "Phone number already taken")
    private String phoneNumber;

}
