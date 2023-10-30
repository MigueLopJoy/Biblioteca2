package com.miguel.library.services;

import com.miguel.library.DTO.BookResponseDTOBookCopy;
import com.miguel.library.DTO.BooksEditDTOBookCopy;
import com.miguel.library.DTO.BooksSaveDTOBookCopy;
import com.miguel.library.DTO.SuccessfulObjectDeletionDTO;
import com.miguel.library.Exceptions.*;
import com.miguel.library.model.BookCopy;
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
    public BookResponseDTOBookCopy saveNewBookCopy(BookCopy bookCopy) {
        BookCopy savedBookCopy;

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

        bookCopy.setBookEdition(savedBookEdition);
        bookCopy.setBarCode(this.generateBarCode());

        savedBookCopy = bookCopyRepository.save(bookCopy);

        return new BookResponseDTOBookCopy(
                "Book Copy Edited Successfully",
                bookCopyRepository.save(savedBookCopy)
        );
    }

    @Override
    public List<BookCopy> findAll() {
        List<BookCopy> allBookCopies = bookCopyRepository.findAll();
        if (allBookCopies.isEmpty()){
            throw new ExceptionNoSearchResultsFound("No book copies were found");
        }
        return allBookCopies;
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
    public List<BookCopy> searchBookEditionCopies(Integer bookEditionId) {
        BookEdition bookEdition = bookEditionService.searchById(bookEditionId);
        if (Objects.isNull(bookEdition)) {
            throw new ExceptionNullObject("Book edition should not be null");
        }

        BookEdition fetchedBookEdition = bookEditionService.searchByISBN(bookEdition.getISBN());

        if (Objects.isNull(fetchedBookEdition)) {
            throw new ExceptionObjectNotFound("Book edition not found");
        }

        List<BookCopy> bookEditionCopies = bookCopyRepository.findByBookEdition(fetchedBookEdition);

        if (bookEditionCopies.isEmpty()) {
            throw new ExceptionNoSearchResultsFound("No Editions were found");
        }

        return bookEditionCopies;
    }

    @Override
    public BookResponseDTOBookCopy editBookCopy(Integer bookCopyId, BooksEditDTOBookCopy bookEdit) {
        String signature = bookEdit.getSignature();
        Long registrationNumber = bookEdit.getRegistrationNumber();
        Character status = bookEdit.getStatus();

        Optional<BookCopy> optionalBookCopy = bookCopyRepository.findById(bookCopyId);

        if (!optionalBookCopy.isPresent()) {
            throw new ExceptionObjectAlreadyExists("Searched book copy not found");
        }

        BookCopy savedBookCopy = optionalBookCopy.get();

        if (Objects.nonNull(signature)) {
            savedBookCopy.setSignature(signature);
        }

        if (Objects.nonNull(registrationNumber)) {
            savedBookCopy.setRegistrationNumber(registrationNumber);
        }

        if (Objects.nonNull(status)) {
            savedBookCopy.setBookCopyStatus(status);
        }

        return new BookResponseDTOBookCopy(
                "Book Copy Edited Successfully",
                bookCopyRepository.save(savedBookCopy)
        );
    }

    @Override
    public SuccessfulObjectDeletionDTO deleteBookCopy(Integer bookCopyId) {
        Optional<BookCopy> optionalBookCopy = bookCopyRepository.findById(bookCopyId);

        if (!optionalBookCopy.isPresent()) {
            throw new ExceptionObjectNotFound("Book Copy Not Found");
        }
        bookCopyRepository.deleteById(bookCopyId);
        return new SuccessfulObjectDeletionDTO("Book Copy Deleted Successfully");
    }

    @Override
    public BookCopy createBookCopyFromBookSaveDTO(BooksSaveDTOBookCopy bookCopy) {
        return BookCopy.builder()
                .barCode(this.generateBarCode())
                .registrationNumber(bookCopy.getRegistrationNumber())
                .registrationDate(LocalDate.now())
                .signature(bookCopy.getSignature())
                .bookCopyStatus('B')
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

        if (Objects.nonNull(bookCopy)) {
            codeAlreadyUsed = true;
        }
        return codeAlreadyUsed;
    }
}
