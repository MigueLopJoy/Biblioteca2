package com.miguel.biblioteca.controllers;

import com.miguel.biblioteca.DTO.LibraryDTO;
import com.miguel.biblioteca.mapper.LibraryMapper;
import com.miguel.biblioteca.model.Library;
import com.miguel.biblioteca.services.IAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthenticationController {
    
    @Autowired
    private IAuthenticationService authenticationService;
    
    @Autowired
    private LibraryMapper libraryMapper;
    
    @PostMapping("/singup-library")
    public ResponseEntity<Library> signUpLibrary(LibraryDTO libraryDTO){
        return ResponseEntity.ok(authenticationService.SignUpNewLibrary(libraryMapper.mapDtoToEntity(libraryDTO)));                
    }
}
