package com.miguel.biblioteca.controllers;

import com.miguel.biblioteca.DTO.AuthenticationRequestDTO;
import com.miguel.biblioteca.DTO.AuthenticationResponseDTO;
import com.miguel.biblioteca.DTO.LibraryRegistrationRequestDTO;
import com.miguel.biblioteca.services.IAuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/auth")
@CrossOrigin("*")
public class authenticationController {
    @Autowired
    private IAuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<String> signUpNewLibrary(
            @Valid @RequestBody LibraryRegistrationRequestDTO request
            ) {
        authenticationService.SignUpNewLibrary(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Registration successful");
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponseDTO> authenticate(
            @RequestBody AuthenticationRequestDTO request
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}
