package com.miguel.library.services;

import com.miguel.library.DTO.AuthorDTO;
import com.miguel.library.Exceptions.ExceptionAuthorAlreadyExists;
import com.miguel.library.Exceptions.ExceptionObjectNotFound;
import com.miguel.library.model.Author;
import com.miguel.library.repository.IAuthorRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ImpAuthorService implements IAuthorService{

    @Autowired
    private IAuthorRepository authorRepository;

    @Autowired
    private EntityManager entityManager;

    @Override
    public Author saveNewAuthor(Author author) {
        Author savedAuthor = null;

        if (author != null) {
            Author fetchedAuthor = this.searchByAuthorName(author);

            if (Objects.nonNull(fetchedAuthor)) {
                throw new ExceptionAuthorAlreadyExists();
            }

            if (fetchedAuthor == null) {
                savedAuthor = authorRepository.save(author);
            }
        }
        return savedAuthor;
    }

    @Override
    public Author searchByAuthorName(Author author) {
        return authorRepository.findByAuthorName(this.getFullAuthorName(author)).orElse(null);
    }

    @Override
    public List<Author> searchByCustomizedSearch(String authorName) {
        return authorRepository.findByCustomizedSearch(authorName);
    }

    @Override
    public Author editAuthor(Integer authorId, String firstName, String lastName) {
        Author editedAuthor = null;

        Optional<Author> optionalAuthor = authorRepository.findById(authorId);

        if (optionalAuthor.isPresent()) {
            Author savedAuthor = optionalAuthor.get();

            if (!StringUtils.isEmpty(firstName) && !firstName.trim().isEmpty()) {
                savedAuthor.setFirstName(firstName);
            }

            if (!StringUtils.isEmpty(lastName) && !firstName.trim().isEmpty()) {
                savedAuthor.setLastName(lastName);
            }

            editedAuthor = this.saveNewAuthor(savedAuthor);
        } else {
            throw new ExceptionObjectNotFound();
        }

        return editedAuthor;
    }

    @Override
    public void deleteAuthor(Integer authorId) {
        Optional<Author> optionalAuthor = authorRepository.findById(authorId);

        if (optionalAuthor.isPresent()) {
            authorRepository.deleteById(authorId);
        }
    }

    @Override
    public Author createAuthorFromDTO(AuthorDTO author) {
        return Author.builder()
                    .firstName(author.getFirstName())
                    .lastName(author.getLastName())
                .build();
    }

    private String getFullAuthorName(Author author) {
        String authorFirstName = author.getFirstName() != null ? author.getFirstName() : "";
        String authorLastName = author.getLastName() != null ? author.getLastName() : "";
        return (authorFirstName + ' ' + authorLastName).trim();
    }
}
