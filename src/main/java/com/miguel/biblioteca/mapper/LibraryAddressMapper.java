package com.miguel.biblioteca.mapper;

import com.miguel.biblioteca.DTO.LibraryAddressDTO;
import com.miguel.biblioteca.model.LibraryAddress;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class LibraryAddressMapper {
    
    @Autowired
    private final ModelMapper modelMapper;

    public LibraryAddress mapDtoToEntity(LibraryAddressDTO libraryAddressDTO) {
        return modelMapper.map(libraryAddressDTO, LibraryAddress.class);
    }
  
    public LibraryAddressDTO mapEntityToDto(LibraryAddress libraryAddress) {
        return modelMapper.map(libraryAddress, LibraryAddressDTO.class);
    }    
}
