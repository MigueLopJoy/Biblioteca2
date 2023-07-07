package com.miguel.biblioteca.services;

import com.miguel.biblioteca.DTO.AuthorDTO;
import com.miguel.biblioteca.model.Author;
import com.miguel.biblioteca.repositories.IAuthorRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorService implements IAuthorService{

    @Autowired
    private IAuthorRepository authorRepository;
    
    @Override
    public Optional<Author> findByAuthorName(String authorName) {
        return authorRepository.findByAuthorName(authorName);
    }

    @Override
    public Author saveNewAuthor(Author author) {
        return authorRepository.save(author);
    }
    
    @Override
    public Author getOrCreateAuthor(Author author) {
        Author searchedAuthor = authorRepository.findByAuthorName(this.getFullAuthorName(author)).orElse(null);
        
        if (searchedAuthor == null) {
            return authorRepository.save(author);
        }           
        return searchedAuthor;
    }
    
    @Override
    public String getFullAuthorName(Author author) {
        String authorFirstName = author.getFirstName() != null ? author.getFirstName() : "";
        String authorLastName = author.getLastNames() != null ? author.getLastNames() : "";
        return (authorFirstName + ' ' + authorLastName).trim();
    }
    
}
