package com.miguel.biblioteca.controllers;

import com.miguel.biblioteca.DTO.AuthorDTO;
import com.miguel.biblioteca.mapper.AuthorMapper;
import com.miguel.biblioteca.model.Author;
import com.miguel.biblioteca.services.IAuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/author")
public class AuthorController {
    
    @Autowired
    private IAuthorService authorService;
    
    @Autowired
    private AuthorMapper authorMapper;
}
