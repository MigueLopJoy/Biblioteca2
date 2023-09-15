package com.miguel.library.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthorDTO {

    @NotBlank(message = "Author's first name required")
    @Pattern(regexp = "^[a-zA-ZÁÉÍÓÚáéíóúÑñ]+(\\s[A-Za-zÁÉÍÓÚáéíóúÑñ]+)*$", message = "Invalid first name provided")
    private String firstName;

    @NotBlank(message = "Author's last name required")
    @Pattern(regexp = "^[a-zA-ZÁÉÍÓÚáéíóúÑñ]+(\\s[A-Za-zÁÉÍÓÚáéíóúÑñ]+)*$", message = "Invalid last name provided")
    private String lastName;
}
