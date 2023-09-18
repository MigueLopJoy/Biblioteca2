package com.miguel.library.services;

import com.miguel.library.DTO.BookEditBookEdition;
import com.miguel.library.DTO.BookSaveBookEdition;
import com.miguel.library.model.BookEdition;
import com.miguel.library.model.BookWork;

import java.util.List;
import java.util.Optional;

public interface IBookEditionService {
    public BookEdition saveNewBookEdition(BookEdition bookEdition);

    public BookEdition searchByISBN(String ISBN);

    public List<BookEdition> searchBookWorkEditions(BookWork bookWork);

    public BookEdition editBookEdition(Integer bookEditionId, BookEditBookEdition bookEdit);

    public String deleteBookEdition(Integer bookEditionId);

    public BookEdition createBookEditionFromBookSaveDTO(BookSaveBookEdition bookEdition);
}
