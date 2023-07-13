package com.miguel.biblioteca.services;

import com.miguel.biblioteca.model.Author;
import com.miguel.biblioteca.model.Book;
import com.miguel.biblioteca.repositories.IBookRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImpBookService implements IBookService{

    @Autowired
    private IBookRepository bookRepository;
    
    @Override
    public List<Book> searchByBookCode(String bookCode){
        return bookRepository.findByBookCode(bookCode);
    }
    
    @Override
    public List<Book> searchByTitle(String title) {
        return bookRepository.findByTitle(title);
    }

    @Override
    public List<Book> searchByAuthor(Author author) {
        return bookRepository.findByAuthor(author);
    }
    
    @Override
    public List<Book> searchByTitleAndAuthor(String title, Author author) {
        return bookRepository.findByTitleAndAuthor(title, author);
    }

    @Override
    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Book saveNewBook(Book book) {
        return bookRepository.save(book);
    }
    
    @Override
    public String generateBookCode(){
        char letter = (char)(Math.random() * (90 - 65 + 1) + 65);
        return "" + (int)(Math.random() * (9999 - 1000 + 1) + 1000) + letter;
    }
}
