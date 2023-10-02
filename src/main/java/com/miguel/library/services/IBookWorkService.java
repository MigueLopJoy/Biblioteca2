package com.miguel.library.services;

import com.miguel.library.DTO.BooksEditDTOBookWork;
import com.miguel.library.DTO.BooksSaveDTOBookWork;
import com.miguel.library.model.Author;
import com.miguel.library.model.BookWork;

import java.util.List;

public interface IBookWorkService {
    public BookWork saveNewBookWork(BookWork bookWork);

    public List<BookWork> findAll();

    public BookWork searchByTitleAndAuthor(BookWork bookWork);

    public List<BookWork> searchAuthorBookWorks(Author author);

    public BookWork editBookWork(Integer idBookWork, BooksEditDTOBookWork bookEdit);

    public String deleteBookWork(Integer idBookWork);

    public BookWork createBookWorkFromBookSaveDTO(BooksSaveDTOBookWork bookWork);
}
