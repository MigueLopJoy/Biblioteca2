package com.miguel.biblioteca.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookSearchResponseDTO {

    private String title;
    private AuthorDTO authorDTO;
    private Integer publicationYear;

    private String editor;
    private Integer editionYear;
}
