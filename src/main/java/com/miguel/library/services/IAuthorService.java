package com.miguel.library.services;

import com.miguel.library.DTO.AuthorDTO;
import com.miguel.library.model.Author;

import java.util.List;

public interface IAuthorService {

    public Author saveNewAuthor(Author author);

    public Author searchByAuthorName(Author author);

    public List<Author> searchByCustomizedSearch(String authorName);

    public Author editAuthor(Integer authorId, String firstName, String lastName);

    public String deleteAuthor(Integer authorId);

    public Author createAuthorFromDTO(AuthorDTO author);

}
