package com.miguel.library.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookEditBookEdition {

    @NotBlank(message = "ISBN required")
    @Pattern(regexp = "^(978|979)-\\d{1,5}-\\d{1,7}-\\d{1,7}-\\d$", message = "Invalid ISBN number")
    private String ISBN;

    @NotBlank(message = "Editor name required")
    private String editor;

    @NotNull(message = "Edition year required")
    @Min(value = 1900, message = "Edition year should not be under 1900")
    private Integer editionYear;

    @NotBlank(message = "Language required")
    @Pattern(regexp = "^[a-zA-ZÁÉÍÓÚáéíóúÑñ]+(\\s[A-Za-zÁÉÍÓÚáéíóúÑñ]+)*$", message = "Invalid language provided")
    private String language;
}
