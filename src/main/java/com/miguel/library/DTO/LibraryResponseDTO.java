package com.miguel.library.DTO;

import com.miguel.library.model.Library;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class LibraryResponseDTO {

    private String successMessage;

    private Library library;
}
