package com.miguel.library.DTO;

import com.miguel.library.Validations.YearNotGreaterThanCurrent;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
@AllArgsConstructor
public class BookSaveBookWork {

    @NotBlank(message = "Title required")
    private String title;

    @NotNull(message = "Author should not be null")
    private AuthorDTO author;

    @NotNull(message = "Publication year required")
    @Min(value = 1900, message = "Publication year should not be under 1900")
    @YearNotGreaterThanCurrent(message = "Publication year should not be greater than current year")
    private Integer publicationYear;

}
