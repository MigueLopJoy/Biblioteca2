package com.miguel.biblioteca.services;

import com.miguel.biblioteca.model.Author;
import java.util.Optional;

public interface IAuthorService {
    public Optional<Author> findByAuthorName(String authorName);   
    public Author saveNewAuthor(Author author);
    public Author getOrCreateAuthor(Author author);    
    public String getFullAuthorName(Author author);    
}
