package com.miguel.library.services;

import com.miguel.library.model.BookCopy;
import com.miguel.library.model.BookEdition;
import com.miguel.library.repository.IBookCopyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ImpBookCopyService implements IBookCopyService{

    @Autowired
    private IBookCopyRepository bookCopyRepository;

    @Autowired
    private IBookEditionService bookEditionService;

    @Override
    public BookCopy saveNewBookCopy(BookCopy bookCopy) {
        BookCopy savedBookCopy = null;

        if (bookCopy != null) {
            BookEdition bookEdition = bookCopy.getBookEdition();

            if (bookEdition != null) {
                BookEdition savedBookEdition = bookEditionService.searchByISBN(bookEdition.getISBN());

                if (savedBookEdition != null) {
                    bookCopy.setBookEdition(savedBookEdition);
                    bookCopy.setBarCode(this.generateBookCode());

                    savedBookCopy = bookCopyRepository.save(bookCopy);
                }
            }
        } else {
            throw new RuntimeException("Book Copy information not provided");
        }
        return savedBookCopy;
    }

    private String generateBookCode(){
        String bookCopyCode;
        char letter;

        do {
            letter = (char)(Math.random() * (90 - 65 + 1) + 65);
            bookCopyCode = "" + (int)(Math.random() * (99999 - 10000 + 1) + 10000) + letter;
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

    @Override
    public BookCopy searchByBarCode(String barCode) {
        return bookCopyRepository.findByBarCode(barCode).orElse(null);
    }

    @Override
    public List<BookCopy> searchBookEditionCopies(BookEdition bookEdition) {
        List<BookCopy> bookEditionCopies = new ArrayList<>();

        if (bookEdition != null) {

            BookEdition fetchedBookEdition = bookEditionService.searchByISBN(bookEdition.getISBN());

            if (fetchedBookEdition != null) {
                bookEditionCopies.addAll(bookCopyRepository.findByBookEdition(fetchedBookEdition));
            }
        }
        return bookEditionCopies;
    }
}
