package com.miguel.library.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthRegisterRequest {

    @NotNull(message = "Library Required")
    private LibraryDTOSaveLibrary library;

    @NotNull(message = "Librarian Required")
    private UserDTOSaveUser librarian;
}
