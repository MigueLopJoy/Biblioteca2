package com.miguel.library.services;

import com.miguel.library.DTO.USearchLibrarianRequest;
import com.miguel.library.DTO.USearchReaderRequest;
import com.miguel.library.model.ULibrarian;
import com.miguel.library.model.UReader;

import java.util.List;

public interface IUSearchService {
    public List<UReader> searchReaders(USearchReaderRequest searchRequest);

    public List<ULibrarian> searchLibrarians(USearchLibrarianRequest searchRequest);}
