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
            Author fetchedAuthor = this.findByAuthorName(author);

            if (fetchedAuthor == null) {
                savedAuthor = authorRepository.save(author);
            } else {
                savedAuthor = fetchedAuthor;
            }
        }
        return savedAuthor;
    }

    @Override
    public Author findByAuthorName(Author author) {
        return authorRepository.findByAuthorName(this.getFullAuthorName(author)).orElse(null);
    }

    @Override
    public Author findByCustomizedSearch(Author author) {
        return null;
    }

    @Override
    public Author editAuthor(Integer authorId, Author author) {
        Author editedAuthor;

        if (author != null) {
            Author savedAuthor = this.findByAuthorName(author);

            if (savedAuthor != null) {

            }
        }


        return author;
    }

    private String getFullAuthorName(Author author) {
        String authorFirstName = author.getFirstName() != null ? author.getFirstName() : "";
        String authorLastName = author.getLastName() != null ? author.getLastName() : "";
        return (authorFirstName + ' ' + authorLastName).trim();
    }
}
