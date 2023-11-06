package com.miguel.library.DTO;

import com.miguel.library.Validations.UniqueEmail;
import com.miguel.library.Validations.UniqueLibraryName;
import com.miguel.library.Validations.UniquePhoneNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@UniqueLibraryName
@UniqueEmail
@UniquePhoneNumber
public class LibraryDTOEditLibrary {

    private Integer originalLibraryId;

    @Pattern(regexp = "^[a-zA-ZÁÉÍÓÚáéíóúÑñ]+(\\s[A-Za-zÁÉÍÓÚáéíóúÑñ]+)*$", message = "Must Introduce a Valid Name")
    private String libraryName;

    @Pattern(regexp = "^\\+?\\d{1,3}[-.\\s]?\\d{1,14}$", message = "Must Provide a Valid Phone Number")
    private String libraryPhoneNumber;

    @Email(message = "Must provide a valid email")
    private String libraryEmail;

    private String libraryAddress;

    @Pattern(regexp = "^[a-zA-ZÁÉÍÓÚáéíóúÑñ]+(\\s[A-Za-zÁÉÍÓÚáéíóúÑñ]+)*$", message = "Must Introduce a Valid City Name")
    private String city;

    @Pattern(regexp = "^[a-zA-ZÁÉÍÓÚáéíóúÑñ]+(\\s[A-Za-zÁÉÍÓÚáéíóúÑñ]+)*$", message = "Must Introduce a Valid Province Name")
    private String province;

    @Pattern(regexp = "^\\d{5}(-\\d{4})?$", message = "Must Provide a Valid Postal Code")
    private String postalCode;
}
