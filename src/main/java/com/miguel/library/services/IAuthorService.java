package com.miguel.library.services;

import com.miguel.library.DTO.AuthorResponseDTO;
import com.miguel.library.DTO.AuthorsDTOEditAuthor;
import com.miguel.library.DTO.AuthorsDTOSaveNewAuthor;
import com.miguel.library.DTO.SuccessfulObjectDeletionDTO;
import com.miguel.library.model.Author;

import java.util.List;

public interface IAuthorService {

    public AuthorResponseDTO saveNewAuthor(Author author);

    public List<Author> findAll();

    public Author searchByAuthorId(Integer authorId);

    public Author searchByAuthorName(Author author);

    public List<Author> searchByCustomizedSearch(String authorName);

    public AuthorResponseDTO editAuthor(Integer authorId, AuthorsDTOEditAuthor authorEdit);

    public SuccessfulObjectDeletionDTO deleteAuthor(Integer authorId);

    public Author createAuthorFromDTO(AuthorsDTOSaveNewAuthor author);

}
