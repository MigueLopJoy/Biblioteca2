package com.miguel.library.DTO;

import com.miguel.library.Validations.EditionYearNotBeforePublicationYear;
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
public class BooksSaveDTOBookEdition {

    @NotBlank(message = "ISBN required")
    @Pattern(regexp = "^(978|979)-\\d{1,5}-\\d{1,7}-\\d{1,7}-\\d$", message = "Invalid ISBN number")
    private String ISBN;

    @NotBlank(message = "Editor name required")
    private String editor;

    @NotNull(message = "Edition year required")
    private Integer editionYear;

    @NotBlank(message = "Language required")
    @Pattern(regexp = "^[a-zA-ZÁÉÍÓÚáéíóúÑñ]+(\\s[A-Za-zÁÉÍÓÚáéíóúÑñ]+)*$", message = "Invalid language provided")
    private String language;

    @NotNull(message = "Book work should not be null")
    private BooksSaveDTOBookWork bookWork;
}
