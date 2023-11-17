package com.miguel.library.DTO;

import com.miguel.library.Validations.UniqueEmail;
import com.miguel.library.Validations.UniquePhoneNumber;
import com.miguel.library.Validations.YearNotGreaterThanCurrent;
import com.miguel.library.model.Role;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@UniquePhoneNumber
@UniqueEmail
public class UserDTOSaveUser {

    @Pattern(regexp = "^[a-zA-ZÁÉÍÓÚáéíóúÑñ]+(\\s[A-Za-zÁÉÍÓÚáéíóúÑñ]+)*$", message = "Must introduce a valid first name")
    private String firstName;

    @Pattern(regexp = "^[a-zA-ZÁÉÍÓÚáéíóúÑñ]+(\\s[A-Za-zÁÉÍÓÚáéíóúÑñ]+)*$", message = "Must introduce a valid last name")
    private String lastName;

    @NotNull(message = "Must select a gender")
    private Character gender;

    @NotNull(message = "Date of birth required")
    @YearNotGreaterThanCurrent(message = "Edition year should not be greater than current year")
    @Min(value = 1900, message = "Edition year should not be under 1900")
    private Integer birthYear;

    @Pattern(regexp = "^\\+?\\d{1,3}[-.\\s]?\\d{1,14}$", message = "Must provide a valid phone number")
    private String phoneNumber;

    @Email(message = "Must provide a valid email")
    private String email;

    @NotBlank(message = "Password Required")
    private String password;
}
