package com.miguel.library.services;

import com.miguel.library.DTO.*;
import com.miguel.library.model.ULibrarian;

import java.util.List;

public interface IULibrarianService {
    public UserDTOLibrarianResponse saveNewLibrarian(ULibrarian uLibrarian);

    public List<ULibrarian> findAll();

    public UserDTOLibrarianResponse editLibrarian(Integer librarianId, UserDTOEditUser librarianEdit);

    public SuccessfulObjectDeletionDTO deleteLibrarian(Integer librarianId);

    public ULibrarian createLibrarianFromDTO(UserDTOSaveLibrarian librarianDTO);
}
