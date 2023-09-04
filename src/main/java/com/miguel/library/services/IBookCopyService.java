package com.miguel.library.services;

import com.miguel.library.model.BookCopy;
import com.miguel.library.model.BookEdition;

import java.util.List;

public interface IBookCopyService {
    public BookCopy saveNewBookCopy(BookCopy bookCopy);

    public BookCopy findByBookCopyCode(String bookCopyCode);

    public List<BookCopy> findBookEditionCopies(BookEdition bookEdition);

}
