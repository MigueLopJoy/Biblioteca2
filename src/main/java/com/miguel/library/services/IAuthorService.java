package com.miguel.library.services;

import com.miguel.library.model.Author;

public interface IAuthorService {

    public Author saveNewAuthor(Author author);

    public Author findByAuthorName(Author author);

    public Author findByCustomizedSearch(Author author);

    public Author editAuthor(Integer authorId, Author author);

}
