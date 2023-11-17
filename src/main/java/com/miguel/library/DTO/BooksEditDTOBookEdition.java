package com.miguel.library.DTO;

import com.miguel.library.Validations.EditionYearNotBeforePublicationYear;
import com.miguel.library.Validations.UniqueISBN;
import com.miguel.library.Validations.YearNotGreaterThanCurrent;
import lombok.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EditionYearNotBeforePublicationYear
@UniqueISBN
public class BooksEditDTOBookEdition {

    @NotNull
    private Integer originalBookEditionId;

    @NotBlank(message = "ISBN required")
    @Pattern(regexp = "^(978|979)-\\d{1,5}-\\d{1,7}-\\d{1,7}-\\d$", message = "Invalid ISBN number")
    private String ISBN;

    @NotBlank(message = "Editor name required")
    private String editor;

    @NotNull(message = "Edition year required")
    @YearNotGreaterThanCurrent(message = "Edition year should not be greater than current year")
    private Integer editionYear;

    @NotBlank(message = "Language required")
    @Pattern(regexp = "^[a-zA-ZÁÉÍÓÚáéíóúÑñ]+(\\s[A-Za-zÁÉÍÓÚáéíóúÑñ]+)*$", message = "Invalid language provided")
    private String language;
}
