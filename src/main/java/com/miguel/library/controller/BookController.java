package com.miguel.library.controller;

import com.miguel.library.DTO.BookSearchRequest;
import com.miguel.library.model.Author;
import com.miguel.library.model.BookCopy;
import com.miguel.library.model.BookEdition;
import com.miguel.library.model.BookWork;
import com.miguel.library.services.IBookCopyService;
import com.miguel.library.services.IBookEditionService;
import com.miguel.library.services.IBookSearchService;
import com.miguel.library.services.IBookWorkService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/books")
public class BookController {

    private final IBookSearchService bookSearchService;

    private final IBookCopyService bookCopyService;

    private final IBookEditionService bookEditionService;

    private final IBookWorkService bookWorkService;

    @PostMapping("/save")
    public ResponseEntity<BookCopy> saveNewBookCopy(
            @RequestBody BookCopy bookCopy
    ) {
        return ResponseEntity.ok(bookCopyService.saveNewBookCopy(bookCopy));
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchBooks(
            @RequestBody BookSearchRequest bookSearchRequest
    ) {
        List<?> results = bookSearchService.searchBooks(bookSearchRequest);

        if (results.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Search Results Found");
        } else {
            return ResponseEntity.ok(results);
        }
    }

    @GetMapping("/search/bookCopies/{bookCopyCode}")
    public ResponseEntity<BookCopy> searchBookCopyByBookCopyCode(
            @PathVariable String bookCopyCode
    ) {
        return ResponseEntity.ok(bookCopyService.findByBookCopyCode(bookCopyCode));
    }

    @GetMapping("/search/authorBookWorks")
    public ResponseEntity<List<BookWork>> searchAuthorBookWorks(
            @RequestBody Author author
    ){
        return ResponseEntity.ok(bookWorkService.findAuthorBookWorks(author));
    }

    @GetMapping("/search/bookWorkEditions")
    public ResponseEntity<List<BookEdition>> searchBookWorkEditions(
            @RequestBody BookWork bookWork
    ){
        return ResponseEntity.ok(bookEditionService.findBookWorkEditions(bookWork));
    }

    @GetMapping("/search/bookEditionCopies")
    public ResponseEntity<List<BookCopy>> searchBookCopiesByBookEdition(
            @RequestBody BookEdition bookEdition
    ){
        return ResponseEntity.ok(bookCopyService.findBookEditionCopies(bookEdition));
    }

}
