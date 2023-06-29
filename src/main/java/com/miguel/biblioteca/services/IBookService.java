package com.miguel.biblioteca.services;

import com.miguel.biblioteca.model.Author;
import com.miguel.biblioteca.model.Book;
import java.util.List;

public interface IBookService {
    public Book findByTitle(String title);
    public List<Book> findByAuthor(Author author);
    public List<Book> findAllBooks();
    public void saveNewBook(Book book);
}
