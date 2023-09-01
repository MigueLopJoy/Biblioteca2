package com.miguel.biblioteca.services;

import com.miguel.biblioteca.DTO.BookCopyDTO;
import com.miguel.biblioteca.mapper.BookMapper;
import com.miguel.biblioteca.model.BookCopy;
import com.miguel.biblioteca.model.BookEdition;
import com.miguel.biblioteca.model.UReader;
import com.miguel.biblioteca.repositories.IBookCopyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@AllArgsConstructor
@Service
public class ImpBookCopyService implements IBookCopyService{

    private final IBookCopyRepository bookCopyRepository;

    private final IBookEditionService bookEditionService;

    private final IUReaderService uReaderService;

    private final BookMapper bookMapper;


    public BookCopyDTO searchByBookCopyCode(String bookCopyCode){
        BookCopyDTO foundBook = null;

        Optional<BookCopy> optionalBookCopy = bookCopyRepository.findByBookCopyCode(bookCopyCode);

        if (optionalBookCopy.isPresent()) {
            foundBook = bookMapper.mapEntityToDto(optionalBookCopy.get());
        }

        return foundBook;
    }

    public BookCopyDTO saveNewBookCopy(BookCopyDTO bookCopyDTO) {
        BookCopy newSavedBookCopy;
        BookCopy bookCopy = bookMapper.mapDtoToEntity(bookCopyDTO);
        BookEdition bookEdition = bookCopy.getBookEdition();

        if (bookEdition != null) {

            bookEditionService.saveNewBookEdition(bookEdition);

            bookCopy.setBookCopyCode(this.generateBookCode());

            newSavedBookCopy = bookCopyRepository.save(bookCopy);
        } else {
            throw new RuntimeException("Book edition information not provided");
        }

        return bookMapper.mapEntityToDto(newSavedBookCopy);
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

        UReader reader = uReaderService.findByReaderCode(code);
        BookCopyDTO bookCopy = this.searchByBookCopyCode(code);

        if (reader != null || bookCopy != null) {
            codeAlreadyUsed = true;
        }

        return codeAlreadyUsed;
    }

}
