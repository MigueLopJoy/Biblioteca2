package com.miguel.library.controller;

import com.miguel.library.DTO.BookEditBookEdition;
import com.miguel.library.DTO.BookSaveBookEdition;
import com.miguel.library.DTO.BookSearchRequestBookEdition;
import com.miguel.library.model.BookEdition;
import com.miguel.library.repository.IBookEditionRepository;
import com.miguel.library.services.IBookEditionService;
import com.miguel.library.services.IBookSearchService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/general-catalog")
public class BookEditionController {

    @Autowired
    private IBookEditionService bookEditionService;

     @Autowired
     private IBookEditionRepository bookEditionRepository;

     @Autowired
     private IBookSearchService bookSearchService;

    @PostMapping("/save-bookedition")
    public ResponseEntity<BookEdition> saveNewBookEdition(
            @Valid @RequestBody BookSaveBookEdition bookEdition
    ) {
        return ResponseEntity.ok(
                bookEditionService.saveNewBookEdition(
                        bookEditionService.createBookEditionFromBookSaveDTO(bookEdition)
                )
        );
    }

    @GetMapping("/get-all")
    public ResponseEntity<?> getAllBookEditions() {
        List<BookEdition> foundBookEditions = bookEditionRepository.findAll();

        if (foundBookEditions.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Search Results Found");
        } else {
            return ResponseEntity.ok(foundBookEditions);
        }
    }

    @GetMapping("/search-bookeditions")
    public ResponseEntity<?> searchBookEditions(
            @RequestBody BookSearchRequestBookEdition bookSearchRequest
        ){
        List<?> foundBookEditions = bookSearchService.searchBooks(bookSearchRequest);

        if (foundBookEditions.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Search Results Found");
        } else {
            return ResponseEntity.ok(foundBookEditions);
        }
    }

    @PutMapping("/edit-bookedition/{bookEditionId}")
    public ResponseEntity<BookEdition> editBookEdition(
            @PathVariable Integer bookEditionId,
            @RequestBody BookEditBookEdition bookEdit
    ) {
        return ResponseEntity.ok(bookEditionService.editBookEdition(bookEditionId, bookEdit));
    }

    @DeleteMapping("/delete-bookedition/{BookEditionId}")
    public ResponseEntity<String> deleteBookEdition(
            @PathVariable Integer bookEditionId
    ) {
        bookEditionService.deleteBookEdition(bookEditionId);
        return ResponseEntity.ok("Book Edtion Deleted Successfully");
    }
}
