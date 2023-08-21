package com.miguel.biblioteca.mapper;

import com.miguel.biblioteca.DTO.LibraryDTO;
import com.miguel.biblioteca.DTO.ULibrarianDTO;
import com.miguel.biblioteca.model.Library;
import com.miguel.biblioteca.model.ULibrarian;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Component
public class LibraryMapper {
    @Autowired
    private final ModelMapper modelMapper;
    
    @Autowired
    private final ULibrarianMapper uLibrarianMapper;

    public Library mapDtoToEntity(LibraryDTO libraryDTO) {

        Library mappedLibrary = modelMapper.map(libraryDTO, Library.class);

        List<ULibrarianDTO> librariansDTO = libraryDTO.getLibrariansDTO();

        if (librariansDTO != null && librariansDTO.size() > 0) {
            List<ULibrarian> librarians = uLibrarianMapper.mapDtoListToEntityList(librariansDTO);
            mappedLibrary.setLibrarians(librarians);
        }

        return mappedLibrary;
    }
  
    public LibraryDTO mapEntityToDto(Library library) {

        LibraryDTO mappedLibraryDTO = modelMapper.map(library, LibraryDTO.class);

        List<ULibrarian> librarians = library.getLibrarians();

        if(librarians != null && librarians.size() > 0) {
            List<ULibrarianDTO> librariansDTO = uLibrarianMapper.mapEntityListToDtoList(librarians);
            mappedLibraryDTO.setLibrariansDTO(librariansDTO);
        }

        return mappedLibraryDTO;
    }    
}
   