package com.miguel.library.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookEditRequest {

    private String title;

    private String author;

    private Integer publicationYear;

    private String ISBN;

    private String editor;

    private Integer editionYear;

}
