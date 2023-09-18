package com.miguel.library.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookSaveBookCopy {

    @NotNull(message = "Registration number required")
    @Min(value = 1, message = "Registration number should be greater than 1")
    private Long registrationNumber;

    @NotBlank(message = "Signature required")
    private String signature;

    @NotNull(message = "Book edition should not be null")
    private BookSaveBookEdition bookEdition;
}








