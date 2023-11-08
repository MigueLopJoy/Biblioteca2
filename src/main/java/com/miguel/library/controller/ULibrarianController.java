package com.miguel.library.controller;

import com.miguel.library.DTO.*;
import com.miguel.library.model.ULibrarian;
import com.miguel.library.services.IULibrarianService;
import com.miguel.library.services.IUSearchService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost/")
@RestController
@RequestMapping("/librarians")
public class ULibrarianController {

    @Autowired
    private IULibrarianService librarianService;

    @Autowired
    private IUSearchService searchService;

    @PostMapping("/save-librarian")
    public ResponseEntity<UserDTOLibrarianResponse> saveNewLibrarian(
            @Valid @RequestBody UserDTOSaveLibrarian librarian
    ) {
        return ResponseEntity.ok(
                librarianService.saveNewLibrarian(
                        librarianService.createLibrarianFromDTO(librarian)
                )
        );
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<ULibrarian>> getAllReaders() {
        return ResponseEntity.ok(librarianService.findAll());
    }

    @PostMapping("/search-librarian")
    public ResponseEntity<List<ULibrarian>> searchReader(
            @Valid @RequestBody UserDTOSearchLibrarianRequest searchRequest
    ) {
        return ResponseEntity.ok(searchService.searchLibrarians(searchRequest));
    }

    @PutMapping("/edit-librarian/{librarianId}")
    public ResponseEntity<UserDTOLibrarianResponse> editLibrarian(
            @PathVariable Integer librarianId,
            @Valid @RequestBody UserDTOEditUser librarianEdit
    ) {
        return ResponseEntity.ok(librarianService.editLibrarian(librarianId, librarianEdit));
    }

    @DeleteMapping("/delete-librarian/{librarianId}")
    public ResponseEntity<SuccessfulObjectDeletionDTO> deleteLibrarian(
            @PathVariable Integer librarianId
    ) {
        return ResponseEntity.ok(librarianService.deleteLibrarian(librarianId));
    }
}
