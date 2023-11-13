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

@RestController
@RequestMapping("/readers")
public class UReaderController {
    @Autowired
    private IUReaderService readerService;

    @Autowired
    private IUSearchService searchService;

    @PostMapping("/save-reader")
    public ResponseEntity<UserDTOReaderResponse> saveNewReader(
            @Valid @RequestBody UserDTOSaveUser reader
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
            @Valid @RequestBody UserDTOSearchReaderRequest searchRequest
            ) {
        return ResponseEntity.ok(searchService.searchReaders(searchRequest));
    }

    @PutMapping("/edit-reader/{readerId}")
    public ResponseEntity<UserDTOReaderResponse> editReader(
            @PathVariable Integer readerId,
            @Valid @RequestBody UserDTOEditReader readerEdit
    ) {
        return ResponseEntity.ok(readerService.editReader(readerId, readerEdit));
    }

    @DeleteMapping("/delete-reader/{readerId}")
    public ResponseEntity<SuccessfulObjectDeletionDTO> deleteReader(
            @PathVariable Integer readerId
    ) {
        return ResponseEntity.ok(readerService.deleteReader(readerId));
    }
}
