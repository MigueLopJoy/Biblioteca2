package com.miguel.biblioteca.services;

import com.miguel.biblioteca.model.Author;
import com.miguel.biblioteca.model.Book;
import java.util.List;

public interface IBookService {
    public List<Book> searchByBookCode(String bookCode);
    public List<Book> searchByTitle(String title);
    public List<Book> searchByAuthor(Author author);    
    public List<Book> searchByTitleAndAuthor(String title, Author author);
    public List<Book> findAllBooks();
    public Book saveNewBook(Book book);
    public String generateBookCode();
    public boolean validateCode();
}
