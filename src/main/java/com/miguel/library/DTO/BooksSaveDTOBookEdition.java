package com.miguel.library.DTO;

import com.miguel.library.Validations.EditionYearNotBeforePublicationYear;
import com.miguel.library.Validations.UniqueISBN;
import com.miguel.library.Validations.YearNotGreaterThanCurrent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@AllArgsConstructor
@Getter @Setter
@EditionYearNotBeforePublicationYear
@UniqueISBN
public class BooksSaveDTOBookEdition {

    @NotBlank(message = "ISBN Required")
    @Pattern(regexp = "^(978|979)-\\d{1,5}-\\d{1,7}-\\d{1,7}-\\d$", message = "Invalid ISBN number")
    private String ISBN;

    @NotBlank(message = "Editor Name Required")
    private String editor;

    @NotNull(message = "Edition Year Required")
    @YearNotGreaterThanCurrent(message = "Edition year should not be greater than current year")
    @Min(value = 1900, message = "Edition year should not be under 1900")
    private Integer editionYear;

    @NotBlank(message = "Language Required")
    @Pattern(regexp = "^[a-zA-ZÁÉÍÓÚáéíóúÑñ]+(\\s[A-Za-zÁÉÍÓÚáéíóúÑñ]+)*$", message = "Invalid language provided")
    private String language;

    @NotNull(message = "Book Work Required")
    private BooksSaveDTOBookWork bookWork;
}
