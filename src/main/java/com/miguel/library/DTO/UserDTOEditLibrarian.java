package com.miguel.library.DTO;

import com.miguel.library.model.Role;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTOEditLibrarian extends UserDTOEditUser {

    @Pattern(regexp = "MANAGER|CATALOGER|LIBRARIAN", message = "Must provide a valid role")
    private Role newRole;
}
