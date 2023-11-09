package com.miguel.library.services;

import com.miguel.library.DTO.LibraryDTOSearchLibrary;
import com.miguel.library.model.Library;

import java.util.List;

public interface ILibrarySearchService {
    public List<Library> searchLibrary(LibraryDTOSearchLibrary searchRequest);
}
