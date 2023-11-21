package com.miguel.library.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTOSearchLibrarianRequest {

    private String librarianName;

    private String email;

    private String mainRole;

    private Integer idLibrary;
}
