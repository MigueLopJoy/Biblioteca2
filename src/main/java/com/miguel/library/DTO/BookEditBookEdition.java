package com.miguel.library.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookEditBookEdition {

    private String ISBN;

    private String editor;

    private Integer editionYear;

    private String language;
}
