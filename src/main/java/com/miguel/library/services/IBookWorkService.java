package com.miguel.library.services;

import com.miguel.library.model.BookCopy;
import com.miguel.library.model.BookWork;

import java.util.Optional;

public interface IBookWorkService {
    public BookWork saveNewBookWork(BookWork bookWork);

}
