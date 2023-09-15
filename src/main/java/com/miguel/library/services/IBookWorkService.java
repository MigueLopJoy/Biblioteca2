package com.miguel.library.services;

import com.miguel.library.DTO.BookEditBookWork;
import com.miguel.library.DTO.BookSaveBookEdition;
import com.miguel.library.DTO.BookSaveBookWork;
import com.miguel.library.model.BookEdition;
import com.miguel.library.model.BookWork;

public interface IBookWorkService {
    public BookWork saveNewBookWork(BookWork bookWork);

    public BookWork searchByTitleAndAuthor(BookWork bookWork);

    public BookWork editBookWork(Integer idBookWork, BookEditBookWork bookEdit);

    public void deleteBookWork(Integer idBookWork);

    public BookWork createBookWorkFromBookSaveDTO(BookSaveBookWork bookWork);
}
