package com.miguel.library.services;

import com.miguel.library.DTO.SuccessfulObjectDeletionDTO;
import com.miguel.library.DTO.UserDTOEditReader;
import com.miguel.library.DTO.UserDTOReaderResponse;
import com.miguel.library.DTO.UserDTOSaveUser;
import com.miguel.library.Exceptions.ExceptionNoSearchResultsFound;
import com.miguel.library.Exceptions.ExceptionNullObject;
import com.miguel.library.Exceptions.ExceptionObjectNotFound;
import com.miguel.library.model.Role;
import com.miguel.library.model.UReader;
import com.miguel.library.repository.IUReaderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ImpUReaderService implements IUReaderService {

    @Autowired
    private IUReaderRepository readerRepository;

    @Override
    public UserDTOReaderResponse saveNewUReader(UReader reader) {
        if (Objects.isNull(reader)) {
            throw new ExceptionNullObject("Reader should not be null");
        }

        reader.setRole(Role.READER);

        return new UserDTOReaderResponse(
                "New Reader Created Successfully",
                readerRepository.save(reader)
        );
    }

    @Override
    public UReader searchByReaderNumber(String readerNumber) {
        return readerRepository.findByReaderNumber(readerNumber).orElse(null);
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
    public UserDTOReaderResponse editReader(Integer readerId, UserDTOEditReader readerEdit) {
        String firstName = readerEdit.getFirstName();
        String lastName = readerEdit.getLastName();
        String email = readerEdit.getEmail();
        String phoneNumber = readerEdit.getPhoneNumber();
        String readerNumber = readerEdit.getReaderNumber();
        Integer birthYear = readerEdit.getBirthYear();
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

        if (Objects.nonNull(birthYear)) {
            fetchedReader.setBirthYear(birthYear);
        }

        if (Objects.nonNull(gender)) {
            fetchedReader.setGender(gender);
        }

        return new UserDTOReaderResponse(
                "Reader Edited Successfully",
                readerRepository.save(fetchedReader)
        );
    }

    @Override
    public SuccessfulObjectDeletionDTO deleteReader(Integer readerId) {
        UReader fetchedReader = this.searchById(readerId);

        if (Objects.isNull(fetchedReader)) {
            throw new ExceptionObjectNotFound("Reader not found");
        }
        readerRepository.deleteById(readerId);
        return new SuccessfulObjectDeletionDTO("Reader Deleted Successfully");
    }

    @Override
    public UReader createReaderFromDTO(UserDTOSaveUser readerDTO) {
        return new UReader(
                readerDTO.getFirstName(),
                readerDTO.getLastName(),
                readerDTO.getGender(),
                readerDTO.getBirthYear(),
                readerDTO.getPhoneNumber(),
                readerDTO.getEmail(),
                readerDTO.getPassword(),
                generateReaderNumber()
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
