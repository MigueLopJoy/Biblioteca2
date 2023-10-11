package com.miguel.library.services;

import com.miguel.library.DTO.UEditReaderDTO;
import com.miguel.library.DTO.USaveReaderDTO;
import com.miguel.library.Exceptions.ExceptionNullObject;
import com.miguel.library.model.UReader;
import com.miguel.library.repository.IUReaderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ImpUReaderService implements IUReaderService{

    @Autowired
    private IUReaderRepository readerRepository;

    @Override
    public UReader saveNewUReader(UReader uReader) {
        if (Objects.isNull(uReader)) {
            throw new ExceptionNullObject("Reader should not be null");
        }

        if (Objects.isNull()) {

        }

        if () {

        }
    }

    @Override
    public UReader searchByReaderNumber(String readerNumber) {
        return readerRepository.findByReaderNumber(readerNumber).orElse(null);
    }

    @Override
    public List<UReader> findAll() {
        return null;
    }

    @Override
    public UReader editReader(Integer readerId, UEditReaderDTO readerEdit) {
        return null;
    }

    @Override
    public String deleteReader(Integer readerId) {
        return null;
    }

    @Override
    public UReader createReaderFromDTO(USaveReaderDTO readerDTO) {
        return new UReader(
                readerDTO.getFirstName(),
                readerDTO.getLastName(),
                readerDTO.getEmail(),
                readerDTO.getPhoneNumber(),
                generateReaderNumber(),
                readerDTO.getDateOfBirth(),
                readerDTO.getGender()
        );
    }

    private String generateReaderNumber() {
        String bookCopyCode;

        do {
            bookCopyCode = "" + (int) (Math.random() * (99999999 - 10000000 + 1) + 10000000);
        } while (isReaderNumberAlreadyUsed(bookCopyCode));

        return bookCopyCode;
    }

    private boolean isReaderNumberAlreadyUsed(String readerNumber) {
        Boolean numberAlreadyUsed = false;

        UReader reader = this.searchByReaderNumber(readerNumber);

        if (Objects.nonNull(reader)) {
            numberAlreadyUsed = true;
        }
        return numberAlreadyUsed;
    }
}
