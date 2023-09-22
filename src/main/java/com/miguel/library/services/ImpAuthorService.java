package com.miguel.library.services;

import com.miguel.library.DTO.AuthorsDTOSaveNewAuthor;
import com.miguel.library.DTO.AuthorsDTOEditAuthor;
import com.miguel.library.Exceptions.ExceptionNoInformationProvided;
import com.miguel.library.Exceptions.ExceptionNullObject;
import com.miguel.library.Exceptions.ExceptionObjectAlreadyExists;
import com.miguel.library.Exceptions.ExceptionObjectNotFound;
import com.miguel.library.model.Author;
import com.miguel.library.repository.IAuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ImpAuthorService implements IAuthorService {

    @Autowired
    private IAuthorRepository authorRepository;

    @Override
    public Author saveNewAuthor(Author author) {
        Author savedAuthor;

        if (Objects.isNull(author)) {
            throw new ExceptionNullObject("Author should not be null");
        }

        Author fetchedAuthor = this.searchByAuthorName(author);

        if (Objects.nonNull(fetchedAuthor)) {
            throw new ExceptionObjectAlreadyExists("Author already exists");
        }

        savedAuthor = authorRepository.save(author);

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
    public Author editAuthor(Integer authorId, AuthorsDTOEditAuthor authorEdit) {
        String firstName = authorEdit.getFirstName();
        String lastName = authorEdit.getLastName();

        Optional<Author> optionalAuthor = authorRepository.findById(authorId);

        if (!optionalAuthor.isPresent()) {
            throw new ExceptionObjectNotFound("Searched author not found");
        }

        Author savedAuthor = optionalAuthor.get();

        if ((StringUtils.isEmpty(firstName) || firstName.trim().isEmpty()) ||
                (StringUtils.isEmpty(lastName) || firstName.trim().isEmpty())
        ) {
            throw new ExceptionNoInformationProvided("Author's new information not provided");
        }

        if (!StringUtils.isEmpty(firstName) && !firstName.trim().isEmpty()) {
            savedAuthor.setFirstName(firstName);
        }

        if (!StringUtils.isEmpty(lastName) && !firstName.trim().isEmpty()) {
            savedAuthor.setLastName(lastName);
        }

        return this.saveNewAuthor(savedAuthor);
    }

    @Override
    public String deleteAuthor(Integer authorId) {
        Optional<Author> optionalAuthor = authorRepository.findById(authorId);

        if (!optionalAuthor.isPresent()) {
            throw new ExceptionObjectNotFound("Searched author not found");
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
