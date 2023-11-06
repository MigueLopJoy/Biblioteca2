package com.miguel.library.services;

import com.miguel.library.DTO.SuccessfulObjectDeletionDTO;
import com.miguel.library.DTO.UserDTOEditReader;
import com.miguel.library.DTO.UserDTOReaderResponse;
import com.miguel.library.DTO.UserDTOSaveUser;
import com.miguel.library.model.UReader;

import java.util.List;

public interface IUReaderService {

    public UserDTOReaderResponse saveNewUReader(UReader uReader);

    public UReader searchByReaderNumber(String readerNumber);

    public List<UReader> findAll();

    public UserDTOReaderResponse editReader(Integer readerId, UserDTOEditReader readerEdit);

    public SuccessfulObjectDeletionDTO deleteReader(Integer readerId);

    public UReader createReaderFromDTO(UserDTOSaveUser readerDTO);
}
