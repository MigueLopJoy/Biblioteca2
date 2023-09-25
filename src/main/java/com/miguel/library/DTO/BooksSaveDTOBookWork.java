package com.miguel.library.DTO;

import com.miguel.library.Validations.YearNotGreaterThanCurrent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter
@AllArgsConstructor
public class BooksSaveDTOBookWork {

    @NotBlank(message = "Title required")
    private String title;

    @NotNull(message = "Author should not be null")
    private AuthorsDTOSaveNewAuthor author;

    @Min(value = 1750, message = "Publication year should not be under 1750")
    @YearNotGreaterThanCurrent(message = "Publication year should not be greater than current year")
    private Integer publicationYear;
}
