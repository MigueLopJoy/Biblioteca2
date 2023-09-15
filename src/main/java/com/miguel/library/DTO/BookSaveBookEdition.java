package com.miguel.library.DTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import javax.validation.constraints.*;
import java.time.LocalDate;


@AllArgsConstructor
@Getter @Setter
public class BookSaveBookEdition {

    @NotBlank
    @Pattern(regexp = "^(978|979)-\\d{1,5}-\\d{1,7}-\\d{1,7}-\\d$", message = "Invalid ISBN number")
    private String ISBN;

    @NotBlank
    private String editor;

    @Min(1900)
    @NotNull
    private Integer editionYear;

    @NotBlank
    private String language;
    @NotNull(message = "Book work should not be null")
    private BookSaveBookWork bookWork;
}
