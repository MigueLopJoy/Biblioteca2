package com.miguel.library.services;

import com.miguel.library.model.BookCopy;
import com.miguel.library.model.BookEdition;

import java.util.List;

public interface IBookCopyService {
    public BookCopy saveNewBookCopy(BookCopy bookCopy);

    public BookCopy searchByBarCode(String barCode);

    public List<BookCopy> searchBookEditionCopies(BookEdition bookEdition);

}
