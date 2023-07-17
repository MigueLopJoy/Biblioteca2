package com.miguel.biblioteca.DTO;

import com.miguel.biblioteca.model.ULibrarian;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
    private ULibrarian user;
    private String jwt;
}

