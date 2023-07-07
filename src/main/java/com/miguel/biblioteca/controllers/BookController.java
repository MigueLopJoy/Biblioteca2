package com.miguel.biblioteca.controllers;

import com.miguel.biblioteca.DTO.AuthorDTO;
import com.miguel.biblioteca.DTO.BookDTO;
import com.miguel.biblioteca.mapper.AuthorMapper;
import com.miguel.biblioteca.mapper.BookMapper;
import com.miguel.biblioteca.model.Author;
import com.miguel.biblioteca.model.Book;
import com.miguel.biblioteca.services.IAuthorService;
import com.miguel.biblioteca.services.IBookService;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/books")
@CrossOrigin(origins = "*")
public class BookController {

    @Autowired
    private IBookService bookService;
    
    @Autowired
    private IAuthorService authorService;    
    
    @Autowired
    private BookMapper bookMapper;
    
    @Autowired
    private AuthorMapper authorMapper;   
    
    @GetMapping("/search")
    public ResponseEntity<List<BookDTO>> searchBooks(@RequestBody BookDTO bookDTO) {
        String bookCode = bookDTO.getBookCode();
        String title = bookDTO.getTitle();  
        String authorName = "";        
        if (bookDTO.getAuthorDTO() != null) {
            authorName = authorService.getFullAuthorName(authorMapper.mapDtoToEntity(bookDTO.getAuthorDTO()));            
        }
        
        if (bookCode != null) {
            return ResponseEntity.ok(bookMapper.mapEntityListToDtoList(bookService.searchByBookCode(bookCode)));
        } else if (title != null && !authorName.equals("")) {
            Author author = authorService.findByAuthorName(authorName).orElse(null);
            if (author != null) {
                return ResponseEntity.ok(bookMapper.mapEntityListToDtoList(bookService.searchByTitleAndAuthor(title, author)));
            }
        } else if (title != null) {
            return ResponseEntity.ok(bookMapper.mapEntityListToDtoList(bookService.searchByTitle(title)));
        } else if (!authorName.equals("")) {
            Author author = authorService.findByAuthorName(authorName).orElse(null);
            if (author != null) {
                return ResponseEntity.ok(bookMapper.mapEntityListToDtoList(bookService.searchByAuthor(author)));                
            }
        }
        return ResponseEntity.ok(Collections.emptyList());
    }
    
    @PostMapping("/save")
    public ResponseEntity<BookDTO> saveBook(@RequestBody BookDTO bookDTO) {        
        Book book = bookMapper.mapDtoToEntity(bookDTO);       
        Author author = authorService.getOrCreateAuthor(book.getAuthor());                    
        book.setAuthor(author);
        Book savedBook = bookService.saveNewBook(book);
        BookDTO savedBookDTO = bookMapper.mapEntityToDto(savedBook);
                       
        return ResponseEntity.ok(savedBookDTO);
    }
}