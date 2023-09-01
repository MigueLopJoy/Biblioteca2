package com.miguel.biblioteca.services;

import com.miguel.biblioteca.model.Author;
import java.util.Optional;

public interface IAuthorService {
    public Optional<Author> findByAuthorName(String authorName);   
    public Author saveNewAuthor(Author author);
    public boolean validateName(String name);
    public String normalizeName(String name);
}
