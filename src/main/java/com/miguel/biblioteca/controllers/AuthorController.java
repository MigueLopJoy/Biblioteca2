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
    
    @GetMapping("search")
    public ResponseEntity<AuthorDTO> searchAuthor(@RequestBody AuthorDTO authorDTO){
        String authorName = authorService.getFullAuthorName(authorMapper.mapDtoToEntity(authorDTO));        
        Author author = authorService.findByAuthorName(authorName).orElse(null);           
        if (author == null) {
            return ResponseEntity.notFound().build();
        }        
        return ResponseEntity.ok(authorMapper.mapEntityToDto(author));                       
    }     
        
    @PostMapping("save")
    public ResponseEntity<AuthorDTO> saveAuthor(@RequestBody AuthorDTO authorDTO) {
        ResponseEntity<AuthorDTO> searchResponse = searchAuthor(authorDTO);
        
        Author author;
        if (searchResponse.getStatusCode().is4xxClientError()){
            author = authorService.saveNewAuthor(authorMapper.mapDtoToEntity(authorDTO));
        } else {
            author =  authorMapper.mapDtoToEntity(authorDTO);
        }       
        return ResponseEntity.ok(authorMapper.mapEntityToDto(author));                     
    }       
}
