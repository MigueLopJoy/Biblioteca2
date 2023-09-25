package com.miguel.library.DTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class BooksEditDTOBookCopy {

    @NotNull(message = "Registration number required")
    @Min(value = 1, message = "Registration number should be greater than 1")
    private Long registrationNumber;

    @NotBlank(message = "Signature required")
    private String signature;

    @Pattern(regexp = "[A-D]", message = "Only 'A', 'B', 'C' or 'D' are accepted status")
    @Size(min = 1, max = 1, message = "Invalid status")
    private Character status;
}
