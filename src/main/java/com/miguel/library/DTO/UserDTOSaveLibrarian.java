package com.miguel.library.DTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTOSaveLibrarian extends UserDTOSaveUser {

    @NotNull(message = "Must Specify a Library")
    private Integer idLibrary;
}
