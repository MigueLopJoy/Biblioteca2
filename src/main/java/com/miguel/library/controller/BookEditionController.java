package com.miguel.library.controller;

import com.miguel.library.DTO.BookResponseDTOBookEdition;
import com.miguel.library.DTO.BooksEditDTOBookEdition;
import com.miguel.library.DTO.BooksSaveDTOBookEdition;
import com.miguel.library.DTO.BookSearchRequestBookEdition;
import com.miguel.library.model.BookEdition;
import com.miguel.library.model.BookWork;
import com.miguel.library.repository.IBookEditionRepository;
import com.miguel.library.services.IBookEditionService;
import com.miguel.library.services.IBookSearchService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost/")
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
    public ResponseEntity<BookResponseDTOBookEdition> saveNewBookEdition(
            @Valid @RequestBody BooksSaveDTOBookEdition bookEdition
    ) {
        return ResponseEntity.ok(
                bookEditionService.saveNewBookEdition(
                        bookEditionService.createBookEditionFromBookSaveDTO(bookEdition)
                )
        );
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<BookEdition>> getAllBookEditions() {
        return ResponseEntity.ok(bookEditionService.findAll());
    }

    @GetMapping("/get-bookwork-editions/{bookWorkId}")
    public ResponseEntity<List<BookEdition>> getAuthorBookWorks(
            @PathVariable Integer bookWorkId
    ) {
        return ResponseEntity.ok(bookEditionService.searchBookWorkEditions(bookWorkId));
    }

    @PostMapping("/search-bookeditions")
    public ResponseEntity<List<?>> searchBookEditions(
            @RequestBody BookSearchRequestBookEdition bookSearchRequest
        ){
        return ResponseEntity.ok(bookSearchService.searchBooks(bookSearchRequest));
    }

    @PutMapping("/edit-bookedition/{bookEditionId}")
    public ResponseEntity<BookResponseDTOBookEdition> editBookEdition(
            @PathVariable Integer bookEditionId,
            @Valid @RequestBody BooksEditDTOBookEdition bookEdit
    ) {
        return ResponseEntity.ok(bookEditionService.editBookEdition(bookEditionId, bookEdit));
    }
    @DeleteMapping("/delete-bookedition/{bookEditionId}")
    public ResponseEntity<String> deleteBookEdition(
            @PathVariable Integer bookEditionId
    ) {
        bookEditionService.deleteBookEdition(bookEditionId);
        return ResponseEntity.ok("Book Edition Deleted Successfully");
    }
}
