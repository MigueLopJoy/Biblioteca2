package com.miguel.library.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookSearchRequestBookEdition extends BookSearchRequestBookWork {

    private String editor;

    private String language;

    private String ISBN;

}
