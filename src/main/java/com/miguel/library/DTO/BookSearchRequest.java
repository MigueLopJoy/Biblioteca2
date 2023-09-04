package com.miguel.library.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class BookSearchRequest {

    private String bookCopyCode;

    private String title;

    private String author;

    private String editor;

    private Integer maxPublicationYear;

    private Integer minPublicationYear;
}
