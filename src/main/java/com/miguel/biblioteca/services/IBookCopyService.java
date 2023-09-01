package com.miguel.biblioteca.services;

import com.miguel.biblioteca.DTO.BookCopyDTO;

public interface IBookCopyService {


    public BookCopyDTO searchByBookCopyCode(String bookCopyCode);
    public BookCopyDTO saveNewBookCopy(BookCopyDTO bookCopyDTO);
}
