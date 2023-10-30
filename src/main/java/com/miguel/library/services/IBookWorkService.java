package com.miguel.library.services;

import com.miguel.library.DTO.BookResponseDTOBookWork;
import com.miguel.library.DTO.BookSearchRequestBookWork;
import com.miguel.library.DTO.BooksEditDTOBookWork;
import com.miguel.library.DTO.BooksSaveDTOBookWork;
import com.miguel.library.model.Author;
import com.miguel.library.model.BookWork;

import java.util.List;

public interface IBookWorkService {
    public BookResponseDTOBookWork saveNewBookWork(BookWork bookWork);

    public List<BookWork> findAll();

    public BookWork searchByBookWorkId(Integer BookWorkId);

    public BookWork searchByTitleAndAuthor(BookWork bookWork);

    public List<BookWork> searchAuthorBookWorks(Integer authorId);

    public BookResponseDTOBookWork editBookWork(Integer idBookWork, BooksEditDTOBookWork bookEdit);

    public String deleteBookWork(Integer idBookWork);

    public BookWork createBookWorkFromDTO(BooksSaveDTOBookWork bookWork);
}
