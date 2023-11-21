package com.miguel.library.services;

import com.miguel.library.DTO.*;
import com.miguel.library.model.Library;
import com.miguel.library.model.Role;
import com.miguel.library.model.ULibrarian;

import java.util.List;

public interface IULibrarianService {
    public UserDTOLibrarianResponse saveNewLibrarian(ULibrarian uLibrarian);

    public UserDTOLibrarianResponse saveNewCataloger(ULibrarian librarian);

    public UserDTOLibrarianResponse saveLibraryManager(ULibrarian librarian);

    public List<ULibrarian> findAll();

    public UserDTOLibrarianResponse editLibrarian(Integer librarianId, UserDTOEditLibrarian librarianEdit);

    public SuccessfulObjectDeletionDTO deleteLibrarian(Integer librarianId);

    public ULibrarian createLibrarianFromDTO(UserDTOSaveUser librarianDTO, Library library);

    public ULibrarian createLibrarianFromDTO(UserDTOSaveLibrarian librarianDTO);

}
