package com.miguel.library.services;

import com.miguel.library.DTO.BookEditBookCopy;
import com.miguel.library.DTO.BookSaveBookCopy;
import com.miguel.library.model.BookCopy;
import com.miguel.library.model.BookEdition;

import java.awt.print.Book;
import java.util.List;

public interface IBookCopyService {
    public BookCopy saveNewBookCopy(BookCopy bookCopy);

    public BookCopy searchByBarCode(String barCode);

    public BookCopy searchByRegistrationNumber(Long registrationNumber);

    public List<BookCopy> searchBookEditionCopies(BookEdition bookEdition);

    public BookCopy editBookCopy(Integer bookCopyId, BookEditBookCopy bookEdit);

    public void deleteBookCopy(Integer bookCopyId);

    public BookCopy createBookCopyFromBookSaveDTO(BookSaveBookCopy bookCopy);

}
