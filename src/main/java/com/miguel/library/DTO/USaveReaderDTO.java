package com.miguel.library.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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

    @Pattern(regexp = "^[a-zA-ZÁÉÍÓÚáéíóúÑñ]+(\\s[A-Za-zÁÉÍÓÚáéíóúÑñ]+)*$", message = "Must introduce a valid first name")
    private String lastName;

    @NotNull
    private LocalDate dateOfBirth;

    @NotNull
    private Character gender;

    @Email(message = "Must provide a valid email")
    private String email;

    @NotBlank(message = "Phone number required")
    @Pattern(regexp = "^\\+?\\d{1,3}\\d{1,14}$", message = "Must provide a valid phone number")
    private String phoneNumber;

}
