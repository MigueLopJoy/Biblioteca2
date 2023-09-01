package com.miguel.biblioteca.services;

import com.miguel.biblioteca.model.Author;
import com.miguel.biblioteca.model.BookWork;

import java.util.List;

public interface IBookWorkService {
    public BookWork saveNewBookWork(BookWork bookWork);

    public List<BookWork> searchByTitle(String title);

    public List<BookWork> searchByAuthor(Author author);

    public BookWork searchByTitleAndAuthor(String title, Author author);

}
