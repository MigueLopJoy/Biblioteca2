package com.miguel.library.controller;

import com.miguel.library.DTO.BookSaveBookCopy;
import com.miguel.library.DTO.BookEditBookCopy;
import com.miguel.library.DTO.BookSearchRequestBookCopy;
import com.miguel.library.model.BookCopy;
import com.miguel.library.model.BookEdition;
import com.miguel.library.repository.IBookCopyRepository;
import com.miguel.library.services.IBookCopyService;
import com.miguel.library.services.IBookSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
            @Valid @RequestBody BookSaveBookCopy bookCopy
    ) {
        return ResponseEntity.ok(
                bookCopyService.saveNewBookCopy(
                        bookCopyService.createBookCopyFromBookSaveDTO(bookCopy)
                )
        );

    }

    @GetMapping("/get-all")
    public ResponseEntity<?> getAllBookCopies() {
        List<BookCopy> foundBookCopies = bookCopyRepository.findAll();

        if (foundBookCopies.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Search Results Found");
        } else {
            return ResponseEntity.ok(foundBookCopies);
        }
    }

    @GetMapping("/search-bookcopies")
    public ResponseEntity<?> searchBookCopies(
            @RequestBody BookSearchRequestBookCopy bookSearchRequest
    ) {
        List<?> foundBookCopies = bookSearchService.searchBooks(bookSearchRequest);

        if (foundBookCopies.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Search Results Found");
        } else {
            return ResponseEntity.ok(foundBookCopies);
        }
    }

    @PutMapping("edit-bookCopy/{bookCopyId}")
    public ResponseEntity<BookEdition> editBookEdition(
            @PathVariable Integer BookCopyId,
            @RequestBody BookEditBookCopy bookEdit
    ) {
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("delete-bookcopy/{bookCopyId}")
    public ResponseEntity<String> deleteBookCopy(
            @PathVariable Integer bookCopyId
    ) {
        return null;
    }
}
