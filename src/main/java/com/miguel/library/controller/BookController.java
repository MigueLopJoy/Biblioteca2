package com.miguel.library.controller;

import com.miguel.library.model.BookCopy;
import com.miguel.library.services.IBookCopyService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/books")
public class BookController {

    private final IBookCopyService bookCopyService;

    @PostMapping("/save")
    public ResponseEntity<BookCopy> saveNewBookCopy(
            @RequestBody BookCopy bookCopy
    ) {
        return ResponseEntity.ok(bookCopyService.saveNewBookCopy(bookCopy));
    }
    @GetMapping("/search/bookCopyCode/{bookCopyCode}")
    public ResponseEntity<BookCopy> searchByBookCopyCode(
            @PathVariable String bookCopyCode) {
        return ResponseEntity.ok(bookCopyService.searchByBookCopyCode(bookCopyCode));
    }
    
}
