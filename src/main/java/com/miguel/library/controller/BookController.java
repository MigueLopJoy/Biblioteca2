package com.miguel.library.controller;

import com.miguel.library.DTO.BookEditRequest;
import com.miguel.library.DTO.BookSearchRequest;
import com.miguel.library.model.BookCopy;
import com.miguel.library.model.BookWork;
import com.miguel.library.services.IBookCopyService;
import com.miguel.library.services.IBookEditionService;
import com.miguel.library.services.IBookSearchService;
import com.miguel.library.services.IBookWorkService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private IBookSearchService bookSearchService;

    @Autowired
    private IBookCopyService bookCopyService;

    @Autowired
    private IBookEditionService bookEditionService;

    @Autowired
    private IBookWorkService bookWorkService;

    @PostMapping("/save")
    public ResponseEntity<BookCopy> saveNewBookCopy(
            @RequestBody BookCopy bookCopy
    ) {
        return ResponseEntity.ok(bookCopyService.saveNewBookCopy(bookCopy));
    }

    @GetMapping("/findAll")
    public ResponseEntity<?> findAll() {
        List<BookWork> results = bookWorkService.findAll();
        return selectReturnResult(results);
    }

    @GetMapping("/search/advanced-search")
    public ResponseEntity<?> searchBooks(
            @RequestBody BookSearchRequest bookSearchRequest
    ) {
        List<?> results = bookSearchService.searchBooks(bookSearchRequest);

        return selectReturnResult(results);
    }

    @GetMapping("/search/bookCopy/{bookCopyCode}")
    public ResponseEntity<?> searchBookCopyByBookCopyCode(
            @PathVariable String bookCopyCode
    ) {
        BookCopy result = bookCopyService.findByBookCopyCode(bookCopyCode);
        return selectReturnResult(Collections.singletonList(result));
    }

    private ResponseEntity<?> selectReturnResult(List<?> results) {
        if (results == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Search Results Found");
        } else {
            return ResponseEntity.ok(results);
        }
    }

}
