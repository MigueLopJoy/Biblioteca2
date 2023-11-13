package com.miguel.library.controller;

import com.miguel.library.DTO.LibraryDTOSaveLibrary;
import com.miguel.library.DTO.LibraryDTOSearchLibrary;
import com.miguel.library.DTO.LibraryResponseDTO;
import com.miguel.library.model.BookCopy;
import com.miguel.library.model.Library;
import com.miguel.library.services.ILibrarySearchService;
import com.miguel.library.services.ILibraryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/library")
public class LibraryController {

    @Autowired
    private ILibrarySearchService searchService;

    @PostMapping("/search-library")
    public ResponseEntity<List<Library>> searchLibrary(
            @Valid @RequestBody LibraryDTOSearchLibrary libraryRequest
    ) {
        return ResponseEntity.ok(searchService.searchLibrary(libraryRequest));
    }
}
