package com.miguel.biblioteca.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BookSearchRequestDTO {
    private String title;
    private AuthorDTO authorDTO;
    private Integer publicationYear;

    public BookSearchRequestDTO(
            String title,
            @JsonProperty("author") AuthorDTO authorDTO,
            Integer publicationYear
    ) {
        this.title = title;
        this.authorDTO = authorDTO;
        this.publicationYear = publicationYear;
    }
}
