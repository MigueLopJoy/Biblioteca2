package com.miguel.biblioteca.services;

import com.miguel.biblioteca.DTO.ULibrarianDTO;
import com.miguel.biblioteca.model.Role;
import com.miguel.biblioteca.model.ULibrarian;

import java.util.Set;

public interface IULibrarianService {
    public ULibrarianDTO signUpNewLibrarian(ULibrarianDTO uLibrarianDTO, Set<Role> authorities);
}
