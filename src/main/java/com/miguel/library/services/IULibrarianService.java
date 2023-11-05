package com.miguel.library.services;

import com.miguel.library.DTO.*;
import com.miguel.library.model.ULibrarian;
import com.miguel.library.model.UReader;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;

public interface IULibrarianService {
    public ULibrarianResponseDTO saveNewLibrarian(ULibrarian uLibrarian);

    public ULibrarian searchByEmail(String email);

    public List<ULibrarian> findAll();

    public ULibrarianResponseDTO editLibrarian(Integer librarianId, UEditLibrarianDTO librarianEdit);

    public SuccessfulObjectDeletionDTO deleteLibrarian(Integer librarianId);

    public ULibrarian createLibrarianFromDTO(USaveLibrarianDTO librarianDTO);
}
