package com.miguel.library.controller;

import com.miguel.library.DTO.AuthorResponseDTO;
import com.miguel.library.DTO.AuthorsDTOEditAuthor;
import com.miguel.library.DTO.AuthorsDTOSaveNewAuthor;
import com.miguel.library.DTO.SuccessfulObjectDeletionDTO;
import com.miguel.library.model.Author;
import com.miguel.library.services.IAuthorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost/")
@RestController
@RequestMapping("/authors-catalog")
public class AuthorController {

    @Autowired
    private IAuthorService authorService;
    @PostMapping("/save-author")
    public ResponseEntity<AuthorResponseDTO> saveNewAuthor(
            @Valid @RequestBody AuthorsDTOSaveNewAuthor author
    ) {
        return ResponseEntity.ok(
                authorService.saveNewAuthor(
                        authorService.createAuthorFromDTO(author)
                )
        );
    }
    @GetMapping("/get-all")
    public ResponseEntity<List<Author>> getAllAuthors() {
        return ResponseEntity.ok(authorService.findAll());
    }

    @GetMapping("/search-author")
    public ResponseEntity<List<Author>> searchAuthor(
            @RequestParam(name = "author_name") String authorName
    ) {
        return ResponseEntity.ok(authorService.searchByCustomizedSearch(authorName));
    }

    @PutMapping("/edit-author/{authorId}")
    public ResponseEntity<AuthorResponseDTO> editAuthor(
            @PathVariable Integer authorId,
            @Valid @RequestBody AuthorsDTOEditAuthor authorEdit
    ) {
        return ResponseEntity.ok(authorService.editAuthor(authorId, authorEdit));
    }

    @DeleteMapping("/delete-author/{authorId}")
    public ResponseEntity<SuccessfulObjectDeletionDTO> deleteAuthor(
            @PathVariable Integer authorId
    ) {
        return ResponseEntity.ok(authorService.deleteAuthor(authorId));
    }
}
