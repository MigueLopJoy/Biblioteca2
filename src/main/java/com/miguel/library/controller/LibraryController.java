package com.miguel.library.controller;

import com.miguel.library.DTO.LibraryDTOSaveLibrary;
import com.miguel.library.DTO.LibraryResponseDTO;
import com.miguel.library.model.Library;
import com.miguel.library.services.ILibraryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost/")
@RestController
@RequestMapping("/library")
public class LibraryController {

    @Autowired
    private ILibraryService libraryService;

    @PostMapping("/save")
    public ResponseEntity<LibraryResponseDTO> saveLibrary(
            @Valid @RequestBody LibraryDTOSaveLibrary library
    ) {
        return ResponseEntity.ok(
                libraryService.saveNewLibrary(
                        libraryService.createLibraryFromDTO(library)
            )
        );
    }
}
