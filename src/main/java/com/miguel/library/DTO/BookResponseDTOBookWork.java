package com.miguel.library.DTO;

import com.miguel.library.model.BookWork;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookResponseDTOBookWork {
    private String successMessage;
    private BookWork bookWork;
}
