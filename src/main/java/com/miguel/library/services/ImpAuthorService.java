package com.miguel.library.services;

import com.miguel.library.model.Author;
import com.miguel.library.repository.IAuthorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class ImpAuthorService implements IAuthorService{

    private final IAuthorRepository authorRepository;


    @Override
    public Author saveNewAuthor(Author author) {
        Author savedAuthor = null;

        if (author != null) {

            Optional<Author> optionalAuthor = authorRepository.findByAuthorName(this.getFullAuthorName(author));

            if (!optionalAuthor.isPresent()) {
                savedAuthor = authorRepository.save(author);
            }

        } else {
            throw  new RuntimeException("Author data not provided");
        }
        return savedAuthor;
    }

    private String getFullAuthorName(Author author) {
        String authorFirstName = author.getFirstName() != null ? author.getFirstName() : "";
        String authorLastName = author.getLastName() != null ? author.getLastName() : "";
        return (authorFirstName + ' ' + authorLastName).trim();
    }
}
