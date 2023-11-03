package com.miguel.library.controller;

import com.miguel.library.DTO.*;
import com.miguel.library.model.UReader;
import com.miguel.library.services.IUReaderService;
import com.miguel.library.services.IUSearchService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost/")
@RestController
@RequestMapping("/readers")
public class UReaderController {
    @Autowired
    private IUReaderService readerService;

    @Autowired
    private IUSearchService searchService;

    @PostMapping("/save-reader")
    public ResponseEntity<UReader> saveNewReader(
            @Valid @RequestBody USaveReaderDTO reader
    ) {
        return ResponseEntity.ok(
                readerService.saveNewUReader(
                        readerService.createReaderFromDTO(reader)
                )
        );
    }
    @GetMapping("/get-all")
    public ResponseEntity<List<UReader>> getAllReaders() {
        return ResponseEntity.ok(readerService.findAll());
    }

    @PostMapping("/search-reader")
    public ResponseEntity<List<UReader>> searchReader(
            @Valid @RequestBody USearchReaderRequest searchRequest
            ) {
        return ResponseEntity.ok(searchService.searchReaders(searchRequest));
    }

    @PutMapping("/edit-reader/{readerId}")
    public ResponseEntity<UReader> editReader(
            @PathVariable Integer readerId,
            @Valid @RequestBody UEditReaderDTO readerEdit
    ) {
        return ResponseEntity.ok(readerService.editReader(readerId, readerEdit));
    }

    @DeleteMapping("/delete-reader/{readerId}")
    public ResponseEntity<String> deleteReader(
            @PathVariable Integer readerId
    ) {
        readerService.deleteReader(readerId);
        return ResponseEntity.ok("Reader deleted successfully");
    }
}
