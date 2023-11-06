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

    public List<Library> searchByCity(String city);

    public List<Library> searchByProvince(String province);

    public LibraryResponseDTO editLibrary(Integer libraryId, LibraryDTOEditLibrary libraryEdit);

    public Library createLibraryFromDTO(LibraryDTOSaveLibrary libraryDTO);
}
