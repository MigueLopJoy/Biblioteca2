package com.miguel.library.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class BooksSaveDTOBookCopy {

    @NotNull(message = "Registration number required")
    @Min(value = 1, message = "Registration number should be greater than 1")
    private Long registrationNumber;

    @NotBlank(message = "Signature required")
    private String signature;

    @NotNull(message = "Book edition should not be null")
    private BooksSaveDTOBookEdition bookEdition;
}








