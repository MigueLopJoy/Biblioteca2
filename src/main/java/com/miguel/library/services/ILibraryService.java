package com.miguel.library.services;

import com.miguel.library.DTO.*;
import com.miguel.library.model.Library;

import java.util.List;

public interface ILibraryService {

    public LibraryResponseDTO saveNewLibrary(Library library);

    public List<Library> findAll();

    public Library searchByLibraryId(Integer libraryId);

    public Library searchByLibraryEmail(String email);

    public Library searchByLibraryPhoneNumber(String phoneNumber);

    public Library searchByLibraryName(String libraryName);


    public Library createLibraryFromDTO(LibraryDTOSaveLibrary libraryDTO);
}
