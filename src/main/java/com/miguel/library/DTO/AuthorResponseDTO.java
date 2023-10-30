package com.miguel.library.DTO;

import com.miguel.library.model.Author;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthorResponseDTO {
    private String successMessage;
    private Author author;
}
