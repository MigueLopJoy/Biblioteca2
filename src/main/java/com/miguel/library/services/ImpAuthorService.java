package com.miguel.library.services;

import com.miguel.library.DTO.AuthorResponseDTO;
import com.miguel.library.DTO.AuthorsDTOSaveNewAuthor;
import com.miguel.library.DTO.AuthorsDTOEditAuthor;
import com.miguel.library.DTO.SuccessfulObjectDeletionDTO;
import com.miguel.library.Exceptions.*;
import com.miguel.library.model.Author;
import com.miguel.library.model.BookWork;
import com.miguel.library.repository.IAuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class ImpAuthorService implements IAuthorService {

    @Autowired
    private IAuthorRepository authorRepository;

    @Autowired
    private IBookWorkService bookWorkService;

    @Override
    public AuthorResponseDTO saveNewAuthor(Author author) {
        if (Objects.isNull(author)) {
            throw new ExceptionNullObject("Author should not be null");
        }

        Author fetchedAuthor = this.searchByAuthorName(author);

        if (Objects.nonNull(fetchedAuthor)) {
            throw new ExceptionObjectAlreadyExists("Author already exists");
        }


        return new AuthorResponseDTO(
                "New Author Created Successfully",
                authorRepository.save(author)
        );
    }

    @Override
    public List<Author> findAll() {
        List<Author> allAuthors = authorRepository.findAll();
        if (allAuthors.isEmpty()) {
            throw new ExceptionNoSearchResultsFound("No authors were found");
        }
        Collections.sort(allAuthors);
        return allAuthors;
    }

    @Override
    public Author searchByAuthorId(Integer authorId) {
        return authorRepository.findById(authorId).orElse(null);
    }

    @Override
    public Author searchByAuthorName(Author author) {
        Author fetchedAuthor = null;
        if (Objects.nonNull(author)) {
            fetchedAuthor = authorRepository.findByAuthorName(this.getFullAuthorName(author)).orElse(null);
        }
        return fetchedAuthor;
    }

    @Override
    public List<Author> searchByCustomizedSearch(String authorName) {
        List<Author> searchResults = authorRepository.findByCustomizedSearch(authorName);
        if (searchResults.isEmpty()) {
            throw new ExceptionNoSearchResultsFound("No search results were found");
        }
        Collections.sort(searchResults);
        return searchResults;
    }

    public Author searchById(Integer authorId) {
        return authorRepository.findById(authorId).orElse(null);
    }

    @Override
    public AuthorResponseDTO editAuthor(Integer authorId, AuthorsDTOEditAuthor authorEdit) {
        String firstName = authorEdit.getFirstName();
        String lastName = authorEdit.getLastName();

        Author fetchedAuthor = this.searchById(authorId);

        if (Objects.isNull(fetchedAuthor)) {
            throw new ExceptionObjectNotFound("Author not found");
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

        return new AuthorResponseDTO(
                "Author Edited Successfully",
                authorRepository.save(fetchedAuthor)
        );
    }

    @Override
    public SuccessfulObjectDeletionDTO deleteAuthor(Integer authorId) {

        Author fetchedAuthor = this.searchById(authorId);
        if (Objects.isNull(fetchedAuthor)) {
            throw new ExceptionObjectNotFound("Author not found");
        }

        try{
            bookWorkService.searchAuthorBookWorks(authorId);
            throw new ExceptionHasRelatedObjects("Cannot Delete Author While Associated Books Exist");
        } catch (ExceptionNoSearchResultsFound ex) {
            authorRepository.deleteById(authorId);
            return new SuccessfulObjectDeletionDTO("Author Deleted Successfully");
        }
    }

    @Override
    public Author createAuthorFromDTO(AuthorsDTOSaveNewAuthor author) {
        return Author.builder()
                .firstName(author.getFirstName())
                .lastName(author.getLastName())
                .build();
    }

    private String getFullAuthorName(Author author) {
        String fullAuthorName = null;
        if (Objects.nonNull(author)) {
            String authorFirstName = author.getFirstName() != null ? author.getFirstName() : "";
            String authorLastName = author.getLastName() != null ? author.getLastName() : "";
            fullAuthorName = (authorFirstName + ' ' + authorLastName).trim();
        }
        return fullAuthorName;
    }
}
