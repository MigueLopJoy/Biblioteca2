package com.miguel.library.services;

import com.miguel.library.DTO.BookEditBookWork;
import com.miguel.library.DTO.BookSaveBookEdition;
import com.miguel.library.DTO.BookSaveBookWork;
import com.miguel.library.model.Author;
import com.miguel.library.model.BookEdition;
import com.miguel.library.model.BookWork;

import java.util.List;

public interface IBookWorkService {
    public BookWork saveNewBookWork(BookWork bookWork);

    public BookWork searchByTitleAndAuthor(BookWork bookWork);

    public List<BookWork> searchAuthorBookWorks(Author author);

    public BookWork editBookWork(Integer idBookWork, BookEditBookWork bookEdit);

    public String deleteBookWork(Integer idBookWork);

    public BookWork createBookWorkFromBookSaveDTO(BookSaveBookWork bookWork);
}
