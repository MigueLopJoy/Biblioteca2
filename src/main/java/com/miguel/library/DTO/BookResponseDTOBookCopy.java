package com.miguel.library.DTO;

import com.miguel.library.model.BookCopy;
import com.miguel.library.model.BookEdition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookResponseDTOBookCopy {
    private String successMessage;
    private BookCopy bookCopy;
}
