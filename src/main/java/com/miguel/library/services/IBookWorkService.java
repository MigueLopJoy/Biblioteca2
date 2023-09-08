package com.miguel.library.services;

import com.miguel.library.model.Author;
import com.miguel.library.model.BookWork;

import java.util.List;

public interface IBookWorkService {
    public BookWork saveNewBookWork(BookWork bookWork);

    public List<BookWork> findAll();

    public BookWork findByTitleAndAuthor(BookWork bookWork);

    public List<BookWork> findAuthorBookWorks(Author author);


}
