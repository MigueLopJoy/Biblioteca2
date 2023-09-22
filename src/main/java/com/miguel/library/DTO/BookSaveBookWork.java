package com.miguel.library.DTO;

import com.miguel.library.Validations.YearNotGreaterThanCurrent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter
@AllArgsConstructor
public class BookSaveBookWork {

    @NotBlank(message = "Title required")
    private String title;

    @NotNull(message = "Author should not be null")
    private AuthorsDTOSaveNewAuthor author;

    @NotNull(message = "Publication year required")
    @Min(value = 1900, message = "Publication year should not be under 1900")
    @YearNotGreaterThanCurrent(message = "Publication year should not be greater than current year")
    private Integer publicationYear;

}
