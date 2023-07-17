package com.miguel.biblioteca.mapper;

import com.miguel.biblioteca.DTO.AuthorDTO;
import com.miguel.biblioteca.model.Author;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class AuthorMapper {

    @Autowired
    private final ModelMapper modelMapper;

    public Author mapDtoToEntity(AuthorDTO authorDTO) {
        return modelMapper.map(authorDTO, Author.class);
    }
  
    public AuthorDTO mapEntityToDto(Author author) {
        return modelMapper.map(author, AuthorDTO.class);
    }
    
    public List<AuthorDTO> mapEntityListToDtoList(List<Author> authors) {
        return authors.stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }
}
