package com.miguel.library.services;

import com.miguel.library.model.BookCopy;
import com.miguel.library.model.BookEdition;
import com.miguel.library.repository.IBookCopyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class ImpBookCopyService implements IBookCopyService{

    private final IBookCopyRepository bookCopyRepository;

    private final IBookEditionService bookEditionService;

    @Override
    public BookCopy saveNewBookCopy(BookCopy bookCopy) {
        BookCopy savedBookCopy;
        BookEdition bookEdition;

        if (bookCopy != null) {
            bookEdition = bookCopy.getBookEdition();

            bookEditionService.saveNewBookEdition(bookEdition);

            bookCopy.setBookCopyCode(this.generateBookCode());

            savedBookCopy = bookCopyRepository.save(bookCopy);
        } else {
            throw new RuntimeException("Book Copy information not provided");
        }

        return savedBookCopy;
    }

    public BookCopy searchByBookCopyCode(String bookCopyCode){
        return bookCopyRepository.findByBookCopyCode(bookCopyCode).orElse(null);
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

        BookCopy bookCopy = this.searchByBookCopyCode(code);

        if (bookCopy != null) {
            codeAlreadyUsed = true;
        }

        return codeAlreadyUsed;
    }
}
