package com.miguel.library.services;

import com.miguel.library.DTO.UEditReaderDTO;
import com.miguel.library.DTO.USaveReaderDTO;
import com.miguel.library.model.UReader;

import java.util.List;

public interface IUReaderService {

    public UReader saveNewUReader(UReader uReader);

    public UReader searchByReaderNumber(String readerNumber);


    public UReader searchByPhoneNumber(String phoneNumber);


    public UReader searchByEmail(String email);


    public List<UReader> findAll();

    public UReader editReader(Integer readerId, UEditReaderDTO readerEdit);

    public String deleteReader(Integer readerId);

    public UReader createReaderFromDTO(USaveReaderDTO readerDTO);
}
