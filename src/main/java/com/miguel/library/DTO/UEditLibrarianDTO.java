package com.miguel.library.DTO;

import com.miguel.library.Validations.UniqueEmail;
import com.miguel.library.Validations.UniquePhoneNumber;
import com.miguel.library.Validations.UniqueReaderNumber;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@UniquePhoneNumber
@UniqueEmail
@UniqueReaderNumber
public class UEditLibrarianDTO {

    @NotNull
    private Integer originalLibrarianId;

    @Pattern(regexp = "^[a-zA-ZÁÉÍÓÚáéíóúÑñ]+(\\s[A-Za-zÁÉÍÓÚáéíóúÑñ]+)*$", message = "Must introduce a valid first name")
    private String firstName;

    @Pattern(regexp = "^[a-zA-ZÁÉÍÓÚáéíóúÑñ]+(\\s[A-Za-zÁÉÍÓÚáéíóúÑñ]+)*$", message = "Must introduce a valid last name")
    private String lastName;

    @Email(message = "Must provide a valid email")
    private String email;
    @NotBlank(message = "Password Required")
    @Size(min = 8, message = "Password must contain at least 8 characters")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).*$",
            message = "Password must contain at least one digit, one uppercase letter, and one special character"
    )
    private String password;

    @Pattern(regexp = "^\\+?\\d{1,3}[-.\\s]?\\d{1,14}$", message = "Must provide a valid phone number")
    private String phoneNumber;
}
