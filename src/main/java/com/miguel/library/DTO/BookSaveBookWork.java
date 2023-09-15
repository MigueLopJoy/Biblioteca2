package com.miguel.library.DTO;

import com.miguel.library.model.Author;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookSaveBookWork {

    @NotBlank(message = "Title should not be blank")
    private String title;

    @NonNull
    private AuthorDTO author;

    @NonNull
    @Min(value = 1900, message = "Invalid publication year")
    private Integer publicationYear;

}
