package com.miguel.biblioteca.controllers;

import com.miguel.biblioteca.DTO.LibraryDTO;
import com.miguel.biblioteca.model.LibraryRegistrationRequest;
import com.miguel.biblioteca.services.ILibraryRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/auth")
@CrossOrigin("*")
public class LibraryRegistrationController {

    @Autowired
    private ILibraryRegistrationService libraryRegistrationService;

    @PutMapping
    public ResponseEntity<LibraryDTO> signUpNewLibrary(
            @Valid @RequestBody LibraryRegistrationRequest request
            ) {
        return ResponseEntity.ok(libraryRegistrationService.SignUpNewLibrary(request.getLibraryDTO()));
    }
}
