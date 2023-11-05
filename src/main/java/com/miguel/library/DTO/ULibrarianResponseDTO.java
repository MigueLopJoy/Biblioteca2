package com.miguel.library.DTO;

import com.miguel.library.model.ULibrarian;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ULibrarianResponseDTO {
    private String successMessage;
    private ULibrarian librarian;
}
