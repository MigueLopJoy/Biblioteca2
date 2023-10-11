package com.miguel.library.DTO;

import com.miguel.library.Validations.UniqueRegistrationNumber;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class BooksEditDTOBookCopy {

    @Min(value = 1, message = "Registration number should be greater than 1")
    @UniqueRegistrationNumber
    private Long registrationNumber;

    private String signature;

    @Pattern(regexp = "^[A-D]$", message = "Must provide a valid status")
    private Character status;
}
