package com.miguel.library.services;

import com.miguel.library.DTO.SuccessfulObjectDeletionDTO;
import com.miguel.library.DTO.UEditReaderDTO;
import com.miguel.library.DTO.UReaderResponseDTO;
import com.miguel.library.DTO.USaveReaderDTO;
import com.miguel.library.model.UReader;

import java.util.List;

public interface IUReaderService {

    public UReaderResponseDTO saveNewUReader(UReader uReader);

    public UReader searchByReaderNumber(String readerNumber);


    public UReader searchByPhoneNumber(String phoneNumber);


    public UReader searchByEmail(String email);


    public List<UReader> findAll();

    public UReaderResponseDTO editReader(Integer readerId, UEditReaderDTO readerEdit);

    public SuccessfulObjectDeletionDTO deleteReader(Integer readerId);

    public UReader createReaderFromDTO(USaveReaderDTO readerDTO);
}
