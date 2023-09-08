package com.miguel.library.controller;

import com.miguel.library.DTO.AuthorDTO;
import com.miguel.library.model.Author;
import com.miguel.library.repository.IAuthorRepository;
import com.miguel.library.services.IAuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authors-catalog")
public class AuthorController {

    @Autowired
    private IAuthorService authorService;

    @Autowired
    private IAuthorRepository authorRepository;

    @PostMapping("/save-author")
    public ResponseEntity<Author> saveNewAuthor(
            @RequestBody Author author
        ) {
        return ResponseEntity.ok(authorService.saveNewAuthor(author));
    }

    @GetMapping("/get-all")
    public ResponseEntity<?> getAllAuthors() {
        return ResponseEntity.ok(authorRepository.findAll());
    }

    @GetMapping("/search-author")
    public ResponseEntity<?> searchAuthor(
            @RequestBody Author author
    ) {
        Author foundAuthor = authorService.findByCustomizedSearch(author);

        if (foundAuthor == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Search Results Found");
        } else {
            return ResponseEntity.ok(foundAuthor);
        }
    }

    @PostMapping("/edit-author/{authorId}")
    public ResponseEntity<Author> editAuthor(
            @PathVariable Integer authorId,
            @RequestBody AuthorDTO authorDTO
        ) {
        return null;
    }

}
