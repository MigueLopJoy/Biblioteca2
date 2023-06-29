package com.miguel.biblioteca.services;

import com.miguel.biblioteca.model.Author;
import com.miguel.biblioteca.model.Book;
import com.miguel.biblioteca.repositories.IBookRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService implements IBookService{

    @Autowired
    private IBookRepository bookRepository;
    
    @Override
    public Book findByTitle(String title) {
        return bookRepository.findByTitle(title);
    }

    @Override
    public List<Book> findByAuthor(Author author) {
        return bookRepository.findByAuthor(author);
    }

    @Override
    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public void saveNewBook(Book book) {
        bookRepository.save(book);
    }
    
}
