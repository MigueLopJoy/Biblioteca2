package com.miguel.library.controller;

import com.miguel.library.DTO.BooksSaveDTOBookCopy;
import com.miguel.library.DTO.BooksEditDTOBookCopy;
import com.miguel.library.DTO.BookSearchRequestBookCopy;
import com.miguel.library.model.BookCopy;
import com.miguel.library.model.BookEdition;
import com.miguel.library.repository.IBookCopyRepository;
import com.miguel.library.services.IBookCopyService;
import com.miguel.library.services.IBookSearchService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Book;
import java.util.List;

@CrossOrigin(origins = "http://localhost/")
@RestController
@RequestMapping("/bookcopies")
public class BookCopyController {

    @Autowired
    private IBookCopyRepository bookCopyRepository;

    @Autowired
    private IBookCopyService bookCopyService;

    @Autowired
    private IBookSearchService bookSearchService;

    @PostMapping("save-bookcopy")
    public ResponseEntity<BookCopy> saveNewBookCopy(
            @Valid @RequestBody BooksSaveDTOBookCopy bookCopy
    ) {
        return ResponseEntity.ok(
                bookCopyService.saveNewBookCopy(
                        bookCopyService.createBookCopyFromBookSaveDTO(bookCopy)
                )
        );

    }

    @GetMapping("/get-all")
    public ResponseEntity<List<BookCopy>> getAllBookCopies() {
        return ResponseEntity.ok(bookCopyService.findAll());
    }

    @PostMapping("/search-bookcopies")
    public ResponseEntity<List<Object>> searchBookCopies(
            @RequestBody BookSearchRequestBookCopy bookSearchRequest
    ) {
        return ResponseEntity.ok(bookSearchService.searchBooks(bookSearchRequest));
    }

    @PutMapping("/edit-bookcopy/{bookCopyId}")
    public ResponseEntity<BookCopy> editBookEdition(
            @PathVariable Integer bookCopyId,
            @Valid @RequestBody BooksEditDTOBookCopy bookEdit
    ) {
        return ResponseEntity.ok(bookCopyService.editBookCopy(bookCopyId, bookEdit));
    }

    @DeleteMapping("/delete-bookcopy/{bookCopyId}")
    public ResponseEntity<String> deleteBookCopy(
            @PathVariable Integer bookCopyId
    ) {
        bookCopyService.deleteBookCopy(bookCopyId);
        return ResponseEntity.ok("Book Copy Deleted Successfully");
    }
}
