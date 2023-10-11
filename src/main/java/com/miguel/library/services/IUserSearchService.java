package com.miguel.library.services;

import com.miguel.library.DTO.USearchReaderDTO;
import com.miguel.library.model.UReader;

import java.util.List;

public interface IUserSearchService {

    public List<UReader> searchReaders(USearchReaderDTO searchRequest);

}
