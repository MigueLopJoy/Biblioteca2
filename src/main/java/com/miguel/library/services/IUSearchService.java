package com.miguel.library.services;

import com.miguel.library.DTO.UserDTOSearchLibrarianRequest;
import com.miguel.library.DTO.UserDTOSearchReaderRequest;
import com.miguel.library.model.ULibrarian;
import com.miguel.library.model.UReader;

import java.util.List;

public interface IUSearchService {
    public List<UReader> searchReaders(UserDTOSearchReaderRequest searchRequest);

    public List<ULibrarian> searchLibrarians(UserDTOSearchLibrarianRequest searchRequest);}
