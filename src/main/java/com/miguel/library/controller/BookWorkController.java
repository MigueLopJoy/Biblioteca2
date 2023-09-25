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

@RestController
@RequestMapping("/bookworks-catalog")
public class BookWorkController {

    @Autowired
    private IBookWorkService bookWorkService;

    @Autowired
    private IBookWorkRepository bookWorkRepository;

    @Autowired
    private IBookSearchService bookSearchService;

    @PostMapping("/save-bookwork")
    public ResponseEntity<BookWork> saveNewBookWork(
            @Valid @RequestBody BooksSaveDTOBookWork bookWork
    ) {
        return ResponseEntity.ok(
                bookWorkService.saveNewBookWork(
                        bookWorkService.createBookWorkFromBookSaveDTO(bookWork)
                )
        );
    }

    @GetMapping("/get-all")
    public ResponseEntity<?> getAllBookWorks() {
        List<BookWork> foundBookWorks = bookWorkRepository.findAll();

        if (foundBookWorks.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Search Results Found");
        } else {
            return ResponseEntity.ok(foundBookWorks);
        }
    }


    @GetMapping("/search-bookwork")
    public ResponseEntity<?> searchBookWorks(
            @Valid @RequestBody BookSearchRequestBookWork bookSearchRequest
    ) {
        List<?> foundBookWorks = bookSearchService.searchBooks(bookSearchRequest);

        if (foundBookWorks.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Search Results Found");
        } else {
            return ResponseEntity.ok(foundBookWorks);
        }
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
