package com.miguel.library.services;

import com.miguel.library.DTO.BooksEditDTOBookEdition;
import com.miguel.library.DTO.BooksSaveDTOBookEdition;
import com.miguel.library.model.BookEdition;
import com.miguel.library.model.BookWork;

import java.awt.print.Book;
import java.util.List;

public interface IBookEditionService {
    public BookEdition saveNewBookEdition(BookEdition bookEdition);

    public List<BookEdition> findAll();

    public BookEdition searchById(Integer bookEditionId);

    public BookEdition searchByISBN(String ISBN);

    public List<BookEdition> searchBookWorkEditions(Integer bookWorkId);

    public BookEdition editBookEdition(Integer bookEditionId, BooksEditDTOBookEdition bookEdit);

    public String deleteBookEdition(Integer bookEditionId);

    public BookEdition createBookEditionFromBookSaveDTO(BooksSaveDTOBookEdition bookEdition);
}
