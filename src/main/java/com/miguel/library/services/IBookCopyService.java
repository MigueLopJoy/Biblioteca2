package com.miguel.library.services;

import com.miguel.library.model.BookCopy;

import java.util.Optional;

public interface IBookCopyService {
    public BookCopy saveNewBookCopy(BookCopy bookCopy);

    public BookCopy searchByBookCopyCode(String bookCopyCode);

}
