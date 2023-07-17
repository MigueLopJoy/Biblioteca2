package com.miguel.biblioteca.mapper;

import com.miguel.biblioteca.DTO.LibraryAddressDTO;
import com.miguel.biblioteca.DTO.LibraryDTO;
import com.miguel.biblioteca.DTO.ULibrarianDTO;
import com.miguel.biblioteca.model.Library;
import com.miguel.biblioteca.model.LibraryAddress;
import com.miguel.biblioteca.model.ULibrarian;
import java.util.List;
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
    private final LibraryAddressMapper libraryAddressMapper;
    
    @Autowired
    private final ULibrarianMapper uLibrarianMapper;

    public Library mapDtoToEntity(LibraryDTO libraryDTO) {
        Library library = modelMapper.map(libraryDTO, Library.class);
        LibraryAddressDTO libraryAdressDTO = libraryDTO.getLibraryAddressDTO();
        List<ULibrarianDTO> uLibrariansDTO = libraryDTO.getLibrarians();
        
        if (libraryAdressDTO != null) {
            LibraryAddress libraryAddress = libraryAddressMapper.mapDtoToEntity(libraryAdressDTO);
            library.setLibraryAddress(libraryAddress);
        }
        
        if (uLibrariansDTO != null) {
            List<ULibrarian> uLibrarians = uLibrarianMapper.mapDtoListToEntityList(uLibrariansDTO);
            library.setLibrarians(uLibrarians);
        }        
        return library;
    }
  
    public LibraryDTO mapEntityToDto(Library library) {
        LibraryDTO libraryDTO = modelMapper.map(library, LibraryDTO.class);
        LibraryAddress libraryAdress = library.getLibraryAddress();
        List<ULibrarian> uLibrarians = library.getLibrarians();
        
        if (libraryAdress != null) {
            LibraryAddressDTO libraryAddressDTO = libraryAddressMapper.mapEntityToDto(libraryAdress);
            libraryDTO.setLibraryAddressDTO(libraryAddressDTO);
        }
        
        if (uLibrarians != null) {
            List<ULibrarianDTO> uLibrariansDTO = uLibrarianMapper.mapEntityListToDtoList(uLibrarians);
            libraryDTO.setLibrarians(uLibrariansDTO);
        }    
        
        return libraryDTO;
    }    
}
   