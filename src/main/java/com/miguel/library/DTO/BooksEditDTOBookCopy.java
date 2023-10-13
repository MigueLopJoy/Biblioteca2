package com.miguel.library.DTO;

import com.miguel.library.Validations.UniqueRegistrationNumber;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@UniqueRegistrationNumber
public class BooksEditDTOBookCopy {

    @NotNull
    private Integer originalBookCopyId;

    @Min(value = 1, message = "Registration number should be greater than 1")
    private Long registrationNumber;

    private String signature;

    private Character status;

    private boolean borrowed;
}
