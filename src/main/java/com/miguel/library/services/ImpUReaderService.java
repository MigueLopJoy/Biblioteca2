package com.miguel.library.services;

import com.miguel.library.DTO.UEditReaderDTO;
import com.miguel.library.DTO.USaveReaderDTO;
import com.miguel.library.Exceptions.ExceptionNoSearchResultsFound;
import com.miguel.library.Exceptions.ExceptionNullObject;
import com.miguel.library.Exceptions.ExceptionObjectNotFound;
import com.miguel.library.model.UReader;
import com.miguel.library.repository.IUReaderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class ImpUReaderService implements IUReaderService{

    @Autowired
    private IUReaderRepository readerRepository;

    @Override
    public UReader saveNewUReader(UReader reader) {
        if (Objects.isNull(reader)) {
            throw new ExceptionNullObject("Reader should not be null");
        }
        return readerRepository.save(reader);
    }

    @Override
    public UReader searchByReaderNumber(String readerNumber) {
        return readerRepository.findByReaderNumber(readerNumber).orElse(null);
    }

    @Override
    public UReader searchByPhoneNumber(String phoneNumber) {
        return readerRepository.findByPhoneNumber(phoneNumber).orElse(null);
    }

    @Override
    public UReader searchByEmail(String email) {
        return readerRepository.findByEmail(email).orElse(null);
    }

    public UReader searchById(Integer readerId) {
        return readerRepository.findById(readerId).orElse(null);
    }

    @Override
    public List<UReader> findAll() {
        List<UReader> allUsers = readerRepository.findAll();
        if (allUsers.isEmpty()) {
            throw new ExceptionNoSearchResultsFound("No readers were found");
        }
        return allUsers;
    }

    @Override
    public UReader editReader(Integer readerId, UEditReaderDTO readerEdit) {
        String firstName = readerEdit.getFirstName();
        String lastName = readerEdit.getLastName();
        String email = readerEdit.getEmail();
        String phoneNumber = readerEdit.getPhoneNumber();
        String readerNumber = readerEdit.getReaderNumber();
        Integer yearOfBirth = readerEdit.getYearOfBirth();
        Character gender = readerEdit.getGender();

        UReader fetchedReader = this.searchById(readerId);
        if (Objects.isNull(fetchedReader)) {
            throw new ExceptionObjectNotFound("Reader not found");
        }

        if (Objects.nonNull(firstName)) {
            fetchedReader.setFirstName(firstName);
        }

        if (Objects.nonNull(lastName)) {
            fetchedReader.setLastName(lastName);
        }

        if (Objects.nonNull(email)) {
            fetchedReader.setEmail(email);
        }

        if (Objects.nonNull(phoneNumber)) {
            fetchedReader.setPhoneNumber(phoneNumber);
        }

        if (Objects.nonNull(readerNumber)) {
            fetchedReader.setReaderNumber(readerNumber);
        }

        if (Objects.nonNull(yearOfBirth)) {
            fetchedReader.setYearOfBirth(yearOfBirth);
        }

        if (Objects.nonNull(gender)) {
            fetchedReader.setGender(gender);
        }

        return readerRepository.save(fetchedReader);
    }

    @Override
    public String deleteReader(Integer readerId) {
        UReader fetchedReader = this.searchById(readerId);

        if (Objects.isNull(fetchedReader)) {
            throw new ExceptionObjectNotFound("Reader not found");
        }
        readerRepository.deleteById(readerId);
        return "Reader deleted successfully";
    }

    @Override
    public UReader createReaderFromDTO(USaveReaderDTO readerDTO) {
        return new UReader(
                readerDTO.getFirstName(),
                readerDTO.getLastName(),
                readerDTO.getEmail(),
                readerDTO.getPhoneNumber(),
                generateReaderNumber(),
                readerDTO.getYearOfBirth(),
                readerDTO.getGender()
        );
    }

    private String generateReaderNumber() {
        String readerNumber;

        do {
            readerNumber = "L" + (int) (Math.random() * (99999999 - 10000000 + 1) + 10000000);
        } while (isReaderNumberAlreadyUsed(readerNumber));

        return readerNumber;
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
