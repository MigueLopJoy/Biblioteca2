package com.miguel.biblioteca.controllers;

import com.miguel.biblioteca.DTO.BookCopyDTO;
import com.miguel.biblioteca.services.IBookCopyService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private IBookCopyService bookCopyService;

    @GetMapping("/search/bookCopyCode")
    public ResponseEntity<BookCopyDTO> searchByBookCopyCode(String bookCopyCode) {
        return ResponseEntity.ok(bookCopyService.searchByBookCopyCode(bookCopyCode));
    }

    @GetMapping("/extra-method")
    public ResponseEntity<String> searchByBookCopyCode() {
        return ResponseEntity.ok("Va bien mi rey");
    }


    @PostMapping("/save")
    public ResponseEntity<BookCopyDTO> saveNewBookCopy(
            @Valid @RequestBody BookCopyDTO bookCopyDTO
    ) {
        return ResponseEntity.ok(bookCopyService.saveNewBookCopy(bookCopyDTO));
    }
}