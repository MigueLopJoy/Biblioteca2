package com.miguel.library.services;

import com.miguel.library.model.BookEdition;
import com.miguel.library.model.BookWork;

import java.util.List;

public interface IBookEditionService {
    public BookEdition saveNewBookEdition(BookEdition bookEdition);

    public BookEdition findByISBN(String ISBN);

    public List<BookEdition> findBookWorkEditions(BookWork bookWork);
}
