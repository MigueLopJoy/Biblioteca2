package com.miguel.library.controller;


import com.miguel.library.DTO.BooksEditDTOBookWork;
import com.miguel.library.DTO.BooksSaveDTOBookWork;
import com.miguel.library.DTO.BookSearchRequestBookWork;
import com.miguel.library.model.BookWork;
import com.miguel.library.repository.IBookWorkRepository;
import com.miguel.library.services.IBookSearchService;
import com.miguel.library.services.IBookWorkService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost/")
@RestController
@RequestMapping("/bookworks-catalog")
public class BookWorkController {

    @Autowired
    private IBookWorkService bookWorkService;

    @Autowired
    private IBookSearchService bookSearchService;

    @PostMapping("/save-bookwork")
    public ResponseEntity<BookWork> saveNewBookWork(
            @Valid @RequestBody BooksSaveDTOBookWork bookWork
    ) {
        return ResponseEntity.ok(
                bookWorkService.saveNewBookWork(
                        bookWorkService.createBookWorkFromDTO(bookWork)
                )
        );
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<BookWork>> getAllBookWorks() {
        return ResponseEntity.ok(bookWorkService.findAll());
    }

    @PostMapping("/search-bookwork")
    public ResponseEntity<List<?>> searchBookWorks(
            @Valid @RequestBody BookSearchRequestBookWork bookSearchRequest
    ) {
        return ResponseEntity.ok(bookSearchService.searchBooks(bookSearchRequest));

    }
    @PutMapping("/edit-bookwork/{bookWorkId}")
    public ResponseEntity<BookWork> editBookWork(
            @PathVariable Integer bookWorkId,
            @Valid @RequestBody BooksEditDTOBookWork bookEdit
            ) {
        return ResponseEntity.ok(bookWorkService.editBookWork(bookWorkId, bookEdit));
    }

    @DeleteMapping("/delete-bookwork/{bookWorkId}")
    public ResponseEntity<String> deleteBookWork(
            @PathVariable Integer bookWorkId
    ) {
        bookWorkService.deleteBookWork(bookWorkId);
        return ResponseEntity.ok("Book Work Deleted Successfully");
    }
}
