package com.miguel.library.controller;

import com.miguel.library.DTO.AuthorDTO;
import com.miguel.library.model.Author;
import com.miguel.library.repository.IAuthorRepository;
import com.miguel.library.services.IAuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/authors-catalog")
public class AuthorController {

    @Autowired
    private IAuthorService authorService;

    @Autowired
    private IAuthorRepository authorRepository;

    @PostMapping("/save-author")
    public ResponseEntity<Author> saveNewAuthor(
            @Valid @RequestBody AuthorDTO author
        ) {
        return ResponseEntity.ok(authorService.saveNewAuthor(authorService.createAuthorFromDTO(author)));
    }

    @GetMapping("/get-all")
    public ResponseEntity<?> getAllAuthors() {
        return ResponseEntity.ok(authorRepository.findAll());
    }

    @GetMapping("/search-author")
    public ResponseEntity<?> searchAuthor(
            @RequestParam(required = true, name = "author-name") String authorName
    ) {
        List<Author> foundAuthors = authorService.searchByCustomizedSearch(authorName);

        if (foundAuthors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Search Results Found");
        } else {
            return ResponseEntity.ok(foundAuthors);
        }
    }

    @PutMapping("/edit-author/{authorId}")
    public ResponseEntity<Author> editAuthor(
            @PathVariable Integer authorId,
            @RequestParam(required = false, name = "first-name") String firstName,
            @RequestParam(required = false, name = "last-name") String lastName
    ) {
        return ResponseEntity.ok(authorService.editAuthor(authorId, firstName, lastName));
    }

    @DeleteMapping("/delete-author/{authorId}")
    public ResponseEntity<String> deleteAuthor(
            @PathVariable Integer authorId
    ) {
        authorService.deleteAuthor(authorId);
        return ResponseEntity.ok("Author deleted successfully");
    }
}
