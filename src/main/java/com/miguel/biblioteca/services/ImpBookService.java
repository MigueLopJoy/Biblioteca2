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

    @Override
    public boolean validateCode() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    /**
     * Valida una cadena de texto contra un formato de codigo de ejemplar o numero de lector
     * establecido por una expresion regular
     *
     * @param code cadena de texto que quiere ser validada contra el formato de codigo establecido
     * @return booleano que indica si el codigo es valido o no
     */
    public static boolean validateCode(String code) {
        boolean codigoValido = false;

        if (code.matches("^[0-9]{4}[A-Z]$")) {
            codigoValido = true;
        } else {
            System.out.println("Codigo no valido");
        }
        return codigoValido;
    }
}
