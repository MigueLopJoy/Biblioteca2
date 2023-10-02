package com.miguel.library.services;

import com.miguel.library.DTO.AuthorsDTOSaveNewAuthor;
import com.miguel.library.DTO.AuthorsDTOEditAuthor;
import com.miguel.library.Exceptions.*;
import com.miguel.library.model.Author;
import com.miguel.library.repository.IAuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ImpAuthorService implements IAuthorService {

    @Autowired
    private IAuthorRepository authorRepository;

    @Override
    public Author saveNewAuthor(Author author) {
        if (Objects.isNull(author)) {
            throw new ExceptionNullObject("Author should not be null");
        }

        Author fetchedAuthor = this.searchByAuthorName(author);

        if (Objects.nonNull(fetchedAuthor)) {
            throw new ExceptionObjectAlreadyExists("Author already exists");
        }

        return authorRepository.save(author);
    }

    @Override
    public List<Author> findAll() {
        List<Author> allAuthors = authorRepository.findAll();
        if (allAuthors.isEmpty()) {
            throw new ExceptionNoSearchResultsFound("No authors were found");
        }
        return allAuthors;
    }

    @Override
    public Author searchByAuthorName(Author author) {
        return authorRepository.findByAuthorName(this.getFullAuthorName(author)).orElse(null);
    }

    @Override
    public List<Author> searchByCustomizedSearch(String authorName) {
        List<Author> searchResults = authorRepository.findByCustomizedSearch(authorName);
        if (searchResults.isEmpty()) {
            throw new ExceptionNoSearchResultsFound("No search results were found");
        }
        return searchResults;
    }

    @Override
    public Author editAuthor(Integer authorId, AuthorsDTOEditAuthor authorEdit) {
        String firstName = authorEdit.getFirstName();
        String lastName = authorEdit.getLastName();

        Optional<Author> optionalAuthor = authorRepository.findById(authorId);

        if (!optionalAuthor.isPresent()) {
            throw new ExceptionObjectNotFound("Author not found");
        }

        Author fetchedAuthor = optionalAuthor.get();

        if (Objects.isNull(firstName) && Objects.isNull(lastName)) {
            throw new ExceptionNoInformationProvided("No information provided. Author cannot be edited.");
        }

        if (Objects.nonNull(firstName)) {
            fetchedAuthor.setFirstName(firstName);
        }

        if (Objects.nonNull(lastName)) {
            fetchedAuthor.setLastName(lastName);
        }

        Author authorWithAuthorName = this.searchByAuthorName(fetchedAuthor);

        if (Objects.nonNull(authorWithAuthorName)) {
            if (!authorWithAuthorName.getIdAuthor().equals(authorId)
            ) {
                throw new ExceptionObjectAlreadyExists("Author already exists");
            }
        }

        return authorRepository.save(fetchedAuthor);
    }

    @Override
    public String deleteAuthor(Integer authorId) {
        Optional<Author> optionalAuthor = authorRepository.findById(authorId);

        if (!optionalAuthor.isPresent()) {
            throw new ExceptionObjectNotFound("Author not found");
        }
        authorRepository.deleteById(authorId);
        return "Author deleted successfully";
    }

    @Override
    public Author createAuthorFromDTO(AuthorsDTOSaveNewAuthor author) {
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
