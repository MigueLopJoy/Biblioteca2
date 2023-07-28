package com.miguel.biblioteca.mapper;

import com.miguel.biblioteca.DTO.LibraryDTO;
import com.miguel.biblioteca.DTO.ULibrarianDTO;
import com.miguel.biblioteca.model.Library;
import com.miguel.biblioteca.model.LibraryAddress;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class LibraryMapper {
    @Autowired
    private final ModelMapper modelMapper;
    
    @Autowired
    private final ULibrarianMapper uLibrarianMapper;

    public Library mapDtoToEntity(LibraryDTO libraryDTO) {
        return modelMapper.map(libraryDTO, Library.class);
    }
  
    public LibraryDTO mapEntityToDto(Library library) {
        return modelMapper.map(library, LibraryDTO.class);
    }    
}
   