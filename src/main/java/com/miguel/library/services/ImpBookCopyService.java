package com.miguel.library.services;

import com.miguel.library.DTO.BookEditBookCopy;
import com.miguel.library.DTO.BookSaveBookCopy;
import com.miguel.library.DTO.BookSaveBookEdition;
import com.miguel.library.Exceptions.ExceptionInvalidObject;
import com.miguel.library.Exceptions.ExceptionNullObject;
import com.miguel.library.Exceptions.ExceptionObjectAlreadyExists;
import com.miguel.library.Exceptions.ExceptionObjectNotFound;
import com.miguel.library.model.BookCopy;
import com.miguel.library.model.BookCopyStatus;
import com.miguel.library.model.BookEdition;
import com.miguel.library.repository.IBookCopyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ImpBookCopyService implements IBookCopyService {

    @Autowired
    private IBookCopyRepository bookCopyRepository;

    @Autowired
    private IBookEditionService bookEditionService;

    @Override
    public BookCopy saveNewBookCopy(BookCopy bookCopy) {
        BookCopy savedBookCopy = null;

        if (Objects.isNull(bookCopy)) {
            throw new ExceptionNullObject("Book copy should not be null");
        }

        BookEdition bookEdition = bookCopy.getBookEdition();

        if (Objects.isNull(bookEdition)) {
            throw new ExceptionNullObject("Book copy's book edition should not be null");
        }

        BookEdition savedBookEdition = bookEditionService.searchByISBN(bookEdition.getISBN());

        if (Objects.isNull(savedBookEdition)) {
            throw new ExceptionObjectNotFound("Book copy's book edition not found");
        }

        BookCopy bookCopyWithRegistrationNumber
                = this.searchByRegistrationNumber(bookCopy.getRegistrationNumber());

        if (Objects.nonNull(bookCopyWithRegistrationNumber)) {
            throw new ExceptionObjectAlreadyExists("Book copy already exists");
        }

        bookCopy.setBookEdition(savedBookEdition);
        bookCopy.setBarCode(this.generateBarCode());

        savedBookCopy = bookCopyRepository.save(bookCopy);

        return savedBookCopy;
    }

    @Override
    public BookCopy searchByBarCode(String barCode) {
        return bookCopyRepository.findByBarCode(barCode).orElse(null);
    }

    @Override
    public BookCopy searchByRegistrationNumber(Long registrationNumber) {
        return bookCopyRepository.findByRegistrationNumber(registrationNumber).orElse(null);
    }

    @Override
    public List<BookCopy> searchBookEditionCopies(BookEdition bookEdition) {
        List<BookCopy> bookEditionCopies = new ArrayList<>();

        if (Objects.isNull(bookEdition)) {
            throw new ExceptionNullObject("Book edition should not be null");
        }

        BookEdition fetchedBookEdition = bookEditionService.searchByISBN(bookEdition.getISBN());

        if (Objects.isNull(fetchedBookEdition)) {
            throw new ExceptionObjectNotFound("Book edition not found");
        }
        bookEditionCopies.addAll(bookCopyRepository.findByBookEdition(fetchedBookEdition));

        return bookEditionCopies;
    }

    @Override
    public BookCopy editBookCopy(Integer bookCopyId, BookEditBookCopy bookEdit) {
        BookCopy editedBookCopy = null;
        String signature = bookEdit.getSignature();
        Long registrationNumber = bookEdit.getRegistrationNumber();
        Character status = bookEdit.getStatus();

        Optional<BookCopy> optionalBookCopy = bookCopyRepository.findById(bookCopyId);

        if (!optionalBookCopy.isPresent()) {
            throw new ExceptionObjectAlreadyExists("Searched book copy not found");
        }

        BookCopy savedBookCopy = optionalBookCopy.get();

        if (!StringUtils.isEmpty(signature)
                && !signature.isBlank()) {
            savedBookCopy.setSignature(signature);
        }

        if (registrationNumber != null) {
            if (!this.isRegistrationNumberAlreadyUsed(registrationNumber)) {
                savedBookCopy.setRegistrationNumber(registrationNumber);
            }
        }

        if (Character.isLetter(status) && Character.isAlphabetic(status)) {
            savedBookCopy.setBookCopyStatus(
                    this.selectBookCopyStatus(status)
            );
        }

        editedBookCopy = this.saveNewBookCopy(savedBookCopy);

        return editedBookCopy;
    }

    @Override
    public String deleteBookCopy(Integer bookCopyId) {
        Optional<BookCopy> optionalBookCopy = bookCopyRepository.findById(bookCopyId);

        if (!optionalBookCopy.isPresent()) {
            throw new ExceptionObjectNotFound("Book copy not found");
        }
        bookCopyRepository.deleteById(bookCopyId);
        return "Book copy deleted successfully";
    }

    @Override
    public BookCopy createBookCopyFromBookSaveDTO(BookSaveBookCopy bookCopy) {
        return BookCopy.builder()
                .barCode(this.generateBarCode())
                .registrationNumber(bookCopy.getRegistrationNumber())
                .registrationDate(LocalDate.now())
                .signature(bookCopy.getSignature())
                .bookCopyStatus(BookCopyStatus.OUT_OF_CIRCULATION)
                .borrowed(false)
                .bookEdition(bookEditionService.createBookEditionFromBookSaveDTO(bookCopy.getBookEdition()))
                .build();
    }

    private String generateBarCode() {
        String bookCopyCode;

        do {
            bookCopyCode = "100" + (int) (Math.random() * (99999 - 10000 + 1) + 10000);
        } while (isCodeAlreadyUsed(bookCopyCode));

        return bookCopyCode;
    }

    private boolean isCodeAlreadyUsed(String code) {
        Boolean codeAlreadyUsed = false;

        BookCopy bookCopy = this.searchByBarCode(code);

        if (bookCopy != null) {
            codeAlreadyUsed = true;
        }
        return codeAlreadyUsed;
    }

    private boolean isRegistrationNumberAlreadyUsed(Long registrationNumber) {
        Boolean registrationNumberAlreadyUsed = false;

        BookCopy bookCopy = this.searchByRegistrationNumber(registrationNumber);

        if (bookCopy != null) {
            registrationNumberAlreadyUsed = true;
        }
        return registrationNumberAlreadyUsed;
    }

    private BookCopyStatus selectBookCopyStatus(Character chosenStatus) {
        BookCopyStatus bookCopyStatus;

        if (chosenStatus.equals('A')){
            bookCopyStatus = BookCopyStatus.IN_CIRCULATION;
        } else if (chosenStatus.equals('B')){
            bookCopyStatus = BookCopyStatus.OUT_OF_CIRCULATION;
        } else if (chosenStatus.equals('C')) {
            bookCopyStatus = BookCopyStatus.LOST;
        } else if (chosenStatus.equals('D')) {
            bookCopyStatus = BookCopyStatus.WITHDRAWN;
        } else {
            throw new ExceptionInvalidObject("The provided status is not valid");
        }
        return bookCopyStatus;
    }
}
