package com.miguel.biblioteca.controllers;

import com.miguel.biblioteca.DTO.BookDTO;
import com.miguel.biblioteca.mapper.BookMapper;
import com.miguel.biblioteca.model.Author;
import com.miguel.biblioteca.model.Book;
import com.miguel.biblioteca.services.IAuthorService;
import com.miguel.biblioteca.services.IBookService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private IBookService bookService;
    
    @Autowired
    private IAuthorService authorService;
    
    @GetMapping("/search")
    public ResponseEntity<List<BookDTO>> searchBooks(BookDTO bookDTO) {
        String title = bookDTO.getTitle();
        String authorName = bookDTO.getAuthorDTO().getFirstName() + ' ' + bookDTO.getAuthorDTO().getLastNames();
        
        if (title != null && authorName != null) {
            Author author = authorService.findByAuthorName(authorName).orElse(null);
            if (author != null) {
                return ResponseEntity.ok(BookMapper.mapDtoToEntity(Collections.emptyList().add(bookService.searchByTitleAndAuthor(title, author))));
            }
        } else if (title != null) {
            for (var result : bookService.searchByTitle(title)) {
                results.add(BookMapper.mapEntityToDto(result));
            }            
            return ResponseEntity.ok(results);
        } else if (authorName != null) {
            Author author = authorService.findByAuthorName(authorName).orElse(null);
            if (author != null) {
                return ResponseEntity.ok(BookMapper.mapDtoToEntity(bookService.searchByAuthor(author))));                
            }
        }
        return Collections.emptyList();
    }
}