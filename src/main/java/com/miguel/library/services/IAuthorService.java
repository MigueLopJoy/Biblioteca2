package com.miguel.library.services;

import com.miguel.library.model.Author;

public interface IAuthorService {

    public Author saveNewAuthor(Author author);

    public Author findByAuthorName(String authorName);

    public Author findByAuthorName(Author author);

}
